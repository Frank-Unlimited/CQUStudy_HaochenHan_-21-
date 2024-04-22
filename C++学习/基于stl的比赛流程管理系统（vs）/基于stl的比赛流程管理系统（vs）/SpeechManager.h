#include<iostream>
#include<fstream>
#pragma once
#define FOR(n) for(int i=0;i<n;i++)
#include<map>
#include<vector>
#include"speaker.h"
#include<algorithm>
#include<deque>
#include<numeric>
#include<functional>
using namespace std;

class SpeechManager {
public:
	SpeechManager();
	~SpeechManager();
	void initSpeech();//��ʼ������
	void show_Menu();
	void exitSystem();
	void creatSpeaker();
	void startSpeech();//��ʼ����
	void speechDraw();//��ǩ
	void speechContest();//��������
	void showScore();//��ʾ�������
	void saveRecord();//�����¼
	void loadRecord();//��������¼
	void showRecord();//��ȡ�����¼
	void clearRecord();//��ռ�¼
	bool fileEmpty;
	map<int, vector<string>>m_Record;
	vector<int>v1;//��һ�ֱ���ѡ�ֱ��
	vector<int>v2;//�ڶ��ֱ���ѡ�ֱ��
	vector<int>vVictory;//ǰ��������ѡ�ֱ��
	map<int, Speaker>m_Speaker;//��ű�ż���Ӧѡ������
	int m_Index;//��������
};