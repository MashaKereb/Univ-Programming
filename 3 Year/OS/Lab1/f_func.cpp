#include <iostream>
#include <thread>


using namespace std;

int f(int x){
    if(x == 10){
        this_thread::sleep_for(*new chrono::duration<double, std::milli>(1000));
        return 0;
    }
    if(x == 22){
        this_thread::sleep_for(*new chrono::duration<double, std::milli>(1000));
        return 800;
    }
    if (x > 30){
        while(true);
    }
    if(x == 30){
        return 0;
    }

    return x*100;
}

int main(int argc, char *argv[]){

    int x = 0;
    cin >> x;

    auto res = f(x);
    std::cout << res <<" ";

    exit(0);
}
