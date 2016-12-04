#include <boost/process.hpp>
#include <boost/iostreams/device/file_descriptor.hpp>
#include <boost/iostreams/stream.hpp>
#include <iostream>
#include <boost/thread/mutex.hpp>


#include <boost/thread.hpp>
#include <boost/chrono.hpp>

using namespace boost::process;
using namespace boost::iostreams;
using namespace boost::process::initializers;
using namespace std;


enum CalculationResult{
    NONE, ONLY_G, ONLY_F, BOTH
};


bool exitPromt(boost::mutex& out_mutex){

    while(true){
        out_mutex.lock();
        cout << "Enter \"c\" to continue - c, \"p\" - continue without prompt, \"s\" - to stop."<<endl;
        char c;
        cin >> c;
        out_mutex.unlock();
        switch (c) {
        case 'p':
            return false;
            break;
        case 's':
            return true;
        default:
            break;
        }

        boost::this_thread::sleep(boost::posix_time::milliseconds(5000));
    }


}

int readValue(){
    int x;
    std::cout << "Enter the x value: ";
    std::cin >> x;
    std::cout << std::endl;
    return x;

}

void sendValueToPipe( boost::process::pipe pipe_in, int x){
    file_descriptor_sink s(pipe_in.sink, close_handle);
    stream<file_descriptor_sink> out(s);
    out << x;
    return;
}


void processTheResult(CalculationResult result, int result_value){
    switch (result) {
    case CalculationResult::NONE:
        cout << "No result have been calculated" << endl;
        cout << "Computation was canceled" << endl;
        break;
    case CalculationResult::ONLY_G:
        cout << "Only g(x) have been calculated. g(x) =" << result_value << endl;
        cout << "Computation was canceled" << endl;
        break;
    case CalculationResult::ONLY_F:
        cout << "Only f(x) have been calculated. f(x) =" << result_value << endl;
        cout << "Computation was canceled" << endl;
        break;
    case CalculationResult::BOTH:
        cout << "g(x)*f(x) = "<< result_value << endl;
        break;
    default:
        break;
    }

}
void wait_calculation(boost::process::pipe& p, boost::mutex& h_mutex, bool& done, int& res){
    file_descriptor_source source(p.source, close_handle);
    stream<file_descriptor_source> in(source);

        int y;
        in >> y;
        h_mutex.lock();
        res = y;
        done = true;
        h_mutex.unlock();


}

int main(){

    int x = readValue();

    // pipe creation and variable sending

   //**************************************************************************************************//

       boost::process::pipe f1 = create_pipe();
       boost::process::pipe f2  = create_pipe();

       file_descriptor_source f_in(f2.source, close_handle);
       sendValueToPipe(f2, x);


       file_descriptor_sink f_out(f1.sink, close_handle);
       execute(
           run_exe("f_func"),
             bind_stdout(f_out),
                   bind_stdin(f_in),
                       close_stderr()

       );

   //**************************************************************************************************//
   //              DO NOT SWAP THIS BLOCKS
   //**************************************************************************************************//

       boost::process::pipe g1 = create_pipe();
       boost::process::pipe g2  = create_pipe();


       file_descriptor_source g_in(g2.source, close_handle);
       sendValueToPipe(g2, x);

       file_descriptor_sink g_out(g1.sink, close_handle);
       execute(
           run_exe("g_func"),
             bind_stdout(g_out),
                   bind_stdin(g_in),
                       close_stderr()

       );

   //**************************************************************************************************//

       // this is required for synchronization
       g_out.close();
       f_out.close();


    boost::condition_variable is_ended;
    volatile CalculationResult result = CalculationResult::NONE;
    volatile int result_value = 0;


    volatile bool all_done = false;
    boost::mutex  res_mutex;
    boost::mutex output_mutex;


    boost::thread user_interruption_thread([&is_ended, &res_mutex, &all_done, &output_mutex]{

       bool res = exitPromt(output_mutex);
       if(res){
           res_mutex.lock();
           all_done = true;
           res_mutex.unlock();
           is_ended.notify_all();

       }


    });

    boost::thread calculation_thread([&g1, &f1, &is_ended, &res_mutex, &all_done, &result, &result_value]{


    boost::mutex f_mutex, g_mutex;
    bool f_done = false,  g_done = false; //volatile
    int g_y, f_y; // volatile


    // waiting for subprocesses' results
     boost::thread f_thread(wait_calculation, boost::ref(f1), boost::ref(f_mutex), boost::ref(f_done), boost::ref(f_y));
     boost::thread g_thread(wait_calculation, boost::ref(g1), boost::ref(g_mutex), boost::ref(g_done), boost::ref(g_y));

     while(true){

            boost::this_thread::sleep(boost::posix_time::milliseconds(100));
         {
             boost::lock_guard<boost::mutex> lock(f_mutex);
             if(f_done){
                 boost::lock_guard<boost::mutex> lock_res(res_mutex);

                 if(result == CalculationResult::ONLY_G){
                     result = CalculationResult::BOTH;
                     result_value *=f_y;
                     all_done = true;
                 }

                 else if(result == CalculationResult::NONE){
                     result = CalculationResult::ONLY_F;
                     result_value = f_y;

                     if(result_value == 0){
                         result = CalculationResult::BOTH;
                         all_done = true;

                     }

                 }
                 // for only one-time result processing
                 f_done = false;

                 if(all_done){
                     res_mutex.unlock();
                     is_ended.notify_all();
                     return;
                 }

             }

         }


            boost::this_thread::sleep(boost::posix_time::milliseconds(100));
            boost::this_thread::interruption_point();
            {
                boost::lock_guard<boost::mutex> lock(g_mutex);
                if(g_done){
                    boost::lock_guard<boost::mutex> lock_res(res_mutex);

                    if(result == CalculationResult::ONLY_F){
                        result = CalculationResult::BOTH;
                        result_value *=g_y;
                        all_done = true;
                    }
                    else if(result == CalculationResult::NONE){
                        result = CalculationResult::ONLY_G;
                        result_value = g_y;

                        if(result_value == 0){
                            result = CalculationResult::BOTH;
                            all_done = true;

                        }

                    }
                    g_done = false;
                    if(all_done){
                        res_mutex.unlock();
                        is_ended.notify_all();
                        return;
                    }

                }

            }
         }

    });


    while(!all_done)
        is_ended.wait(*new boost::unique_lock<boost::mutex>(res_mutex));


    output_mutex.lock();
    processTheResult(result, result_value);
    output_mutex.unlock();

    exit(0);

}

