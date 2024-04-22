#include<bits/stdc++.h>
using namespace std;

class Clock{
	public:
		Clock(int newH, int newS, int newM){
			newH = 0;
			newS = 0;
			newM = 0;
		} 
		void setTime(int newH=0, int newM=0, int newS=0);
	private:
		int hour, minute, second;
	public:
		void showTime();
};

inline void Clock::showTime(){
	cout<<hour<<":"<<minute<<":"<<second<<endl;
}

void Clock::setTime(int newH, int newM, int newS){
	hour = newH;
	minute = newM;
	second = newS;
}

int main(){
	
}

 
