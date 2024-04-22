#include<bits/stdc++.h>
using namespace std;

class Clock{
	public:
		Clock(int newH=0, int newM=0, int newS=0):hour(newH),minute(newM),second(newS){}; 
		void showTime(){
			cout<<hour<<":"<<minute<<":"<<second<<endl;
		}
	private:
		int hour, minute, second;
};

class Clockshop{
	public:
		Clockshop(int id, Clock c1, Clock c2, Clock c3):id(id),clocks1(c1),clocks2(c2),clocks3(c3){};
		void showClock(){
			cout<<"Shop"<<id<<":"<<endl;
			cout<<"Clock1: ";clocks1.showTime();
			cout<<"Clock2: ";clocks2.showTime();
			cout<<"Clock3: ";clocks3.showTime();
		}
	private:
		Clock clocks1,clocks2,clocks3;
		int id;
};


int main(){
	Clock myClock(18,30,25);
	myClock.showTime();
	Clock c2,c3;
	Clockshop myShop(1,myClock,c2,c3);
	myShop.showClock();
}
