#include<iostream>
#include "SpeechManager.h"
#define FOR(n) for(int i=0;i<n;i++)
using namespace std;

int main() {
	srand((unsigned int)time(NULL));
	SpeechManager sm;
	int cmd = 0;

	while (1) {
		sm.show_Menu();

		cout << "请输入您的选择:" << endl;
		cin >> cmd;

		switch (cmd) {
		case 1:	//开始比赛
			sm.startSpeech();
			break;
		case 2: //查看记录
			sm.showRecord();
			break;
		case 3: //清空记录
			sm.clearRecord();
			break;
		case 0: //退出系统
			sm.exitSystem();
		default:
			system("cls");//清屏
			break;
		}
	}

	system("pause");
	return 0;
}