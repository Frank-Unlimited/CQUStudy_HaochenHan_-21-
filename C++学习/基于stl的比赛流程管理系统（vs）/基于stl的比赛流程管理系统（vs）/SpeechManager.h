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
	void initSpeech();//初始化操作
	void show_Menu();
	void exitSystem();
	void creatSpeaker();
	void startSpeech();//开始比赛
	void speechDraw();//抽签
	void speechContest();//比赛函数
	void showScore();//显示比赛结果
	void saveRecord();//保存记录
	void loadRecord();//存放往届记录
	void showRecord();//读取往届记录
	void clearRecord();//清空记录
	bool fileEmpty;
	map<int, vector<string>>m_Record;
	vector<int>v1;//第一轮比赛选手编号
	vector<int>v2;//第二轮比赛选手编号
	vector<int>vVictory;//前三名比赛选手编号
	map<int, Speaker>m_Speaker;//存放编号及对应选手容器
	int m_Index;//比赛轮数
};