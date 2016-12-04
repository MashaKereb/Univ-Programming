#include <iostream>
#include <thread>

using namespace std;

int g(int x){
    if(x == 25){
        this_thread::sleep_for(*new chrono::duration<double, std::milli>(1000));
        return 50;
    }
    if(x == 40){
        this_thread::sleep_for(*new chrono::duration<double, std::milli>(1000));
        return 0;
    }
    if(x == 20){
        return 0;
    }
    if(x < 20){
        while(true);
    }

    return x*2;
}

int main(int argc, char *argv[]){

    int x = 0;
    cin >> x;

    auto res = g(x);
    std::cout << res;

    exit(0);
}
