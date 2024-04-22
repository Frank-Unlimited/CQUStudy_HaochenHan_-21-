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

		cout << "����������ѡ��:" << endl;
		cin >> cmd;

		switch (cmd) {
		case 1:	//��ʼ����
			sm.startSpeech();
			break;
		case 2: //�鿴��¼
			sm.showRecord();
			break;
		case 3: //��ռ�¼
			sm.clearRecord();
			break;
		case 0: //�˳�ϵͳ
			sm.exitSystem();
		default:
			system("cls");//����
			break;
		}
	}

	system("pause");
	return 0;
}