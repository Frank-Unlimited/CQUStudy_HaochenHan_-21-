#include "SpeechManager.h"


SpeechManager::SpeechManager() {
	//初始化属性
	this->initSpeech();
	//创建选手
	this->creatSpeaker();
	//加载往届记录
	this->loadRecord();
}
SpeechManager::~SpeechManager() {
	
}
void SpeechManager::show_Menu() {
	cout << "------------------------------------" << endl;
	cout << "欢迎参加重庆大学2022年7月羽毛球比赛" << endl;
	cout << "1. 开始羽毛球比赛" << endl;
	cout << "2. 查看往届记录" << endl;
	cout << "3. 清空比赛记录" << endl;
	cout << "0. 退出比赛程序" << endl;
	cout << "------------------------------------" << endl;
	cout << endl;
}
void SpeechManager::exitSystem() {
	cout << "欢迎下次使用" << endl;
	system("pause");
	exit(0);
}
void SpeechManager::initSpeech() {
	//容器制空
	this->m_Record.clear();
	this->v1.clear();
	this->v2.clear();
	this->vVictory.clear();
	this->m_Speaker.clear();
	//比赛轮数初始化
	this->m_Index = 1;
}
void SpeechManager::creatSpeaker() {
	string nameSeed[12] = { "刘昕恒","韩昊辰","徐友婷","江蓝","金楠","蔡徐坤"
		,"严楷铭","林丹","陶菲克","李宗伟","李雪芮","盖德" };
	for (int i = 0; i < 12; i++) {
		Speaker sp;
		sp.name = "选手";
		sp.name += nameSeed[i];//创建选手姓名
		FOR(2) sp.score[i] = 0;//初始化选手得分
		this->v1.push_back(i + 10001);//创建选手编号，放入v1
		this->m_Speaker.insert(make_pair(i + 10001, sp));
	}
}
void SpeechManager::speechDraw() {
	if (this->m_Index == 1) {
		cout << "第【" << m_Index << "】轮比赛正在抽签" << endl;
		random_shuffle(v1.begin(), v1.end());//打乱顺序
		cout << "------------------------------------" << endl;
		cout << "抽签结果如下：" << endl;
		for (vector<int>::iterator it = v1.begin(); it != v1.end(); it++) {
			cout << *it << " "<<this->m_Speaker[*it].name<<endl;
		}
		cout << endl;
	}
	else {
		cout << "第【" << m_Index << "】轮比赛正在抽签" << endl;
		random_shuffle(v2.begin(), v2.end());
		cout << "------------------------------------" << endl;
		cout << "抽签结果如下：" << endl;
		for (vector<int>::iterator it = v2.begin(); it != v2.end(); it++) {
			cout << *it << " "<<this->m_Speaker[*it].name << endl;
		}
		cout << endl;
	}
	cout << "------------------------------------" << endl;
	system("pause");
	cout << endl;
}
void SpeechManager::speechContest() {
	cout << "----------------第【" << this->m_Index << "】轮比赛正式开始-----------------" << endl;
	vector<int>v_Src;//比赛选手容器
	if (this->m_Index == 1) v_Src = v1;
	else v_Src = v2;

	int num = 0;//记录已经比赛了的选手数量
	multimap<double, int, greater<double>> groupScpre;//临时容器存放小组成绩

	for (vector<int>::iterator it = v_Src.begin(); it != v_Src.end(); it++) {//每个选手依次比赛
		num++;
		//评委打分
		deque<double>d;
		FOR(10) {
			double score = (rand() % 400 + 600) / 10.f;//600~1000
			//cout << score << " ";
			d.push_back(score);
		}
		//cout << endl;
		//对d进行操作得到平均分：
		sort(d.begin(), d.end(), greater<double>());
		d.pop_back();
		d.pop_front();
		double sum = accumulate(d.begin(), d.end(), 0.0f);
		double avg = sum / (double)d.size();
		//cout << "编号：" << *it << " 姓名：" << this->m_Speaker[*it].name << " 平均分：" << avg << endl;

		//*it是选手编号
		m_Speaker[*it]/*取出编号对应的Speaker*/.score[this->m_Index - 1] = avg;//将avg赋值给对应选手
		groupScpre.insert(make_pair(avg, *it));
		if (num % 6 == 0) {
			cout << "第【" << num / 6 << "】组比赛名次：" << endl;
			for (multimap<double, int, greater<double>>::iterator it = groupScpre.begin(); it != groupScpre.end(); it++) {
				cout << "编号：" << it->second << " 姓名：" << m_Speaker[it->second].name << " 平均分："
					<< m_Speaker[it->second].score[this->m_Index - 1] << endl;
			}
			//取走前三名
			int count = 0;
			for (multimap<double, int, greater<double>>::iterator it = groupScpre.begin(); it != groupScpre.end() && count < 3; count++, it++) {
				if (this->m_Index == 1) this->v2.push_back((*it).second);
				else this->vVictory.push_back((*it).second);
			}
			groupScpre.clear();
			cout << endl;
		}
	}
	cout << "----------------第【" << this->m_Index << "】轮比赛结束-----------------" << endl;
	system("pause");
}
void SpeechManager::showScore() {
	cout << "-----------第【" << this->m_Index << "】轮比赛晋级选手信息如下：" << endl;
	vector<int>v;
	//选择v遍历的容器：
	if (this->m_Index == 1) v = v2;
	else v = vVictory;
	for (vector<int>::iterator it = v.begin();it != v.end(); it++) {
		cout << "编号：" << *it << " 姓名：" << this->m_Speaker[*it].name << " 平均分："
			<< this->m_Speaker[*it].score[m_Index - 1] << endl;
	}
	system("pause");
	system("cls");
	this->show_Menu();
}
void SpeechManager::saveRecord() {
	ofstream ofs;
	ofs.open("speech.csv", ios::out | ios::app);//追加的方式写文件
	for (vector<int>::iterator it = vVictory.begin(); it != vVictory.end(); it++) {
		ofs << m_Speaker[*it].name<< "," << m_Speaker[*it].score[1] << ",";
	}
	ofs << endl;
	ofs.close();
	cout << "记录已经保存" << endl;
	this->fileEmpty = 0;
}
void SpeechManager::startSpeech() {
	//第一轮比赛
	//抽签
	this->speechDraw();
	//比赛
	this->speechContest();
	//显示结果
	this->showScore();
	//第二轮比赛
	this->m_Index++;
	//抽签
	this->speechDraw();
	//比赛
	this->speechContest();
	//显示结果
	this->showScore();
	//保存分数
	this->saveRecord();
	//重置比赛，获取记录
	this->initSpeech();
	this->creatSpeaker();
	this->loadRecord();

	cout << "本届比赛完毕" << endl;

	system("pause");
	system("cls");
}
void SpeechManager::loadRecord() {
	ifstream ifs("speech.csv", ios::in);//读文件
	if (!ifs.is_open()) {
		this->fileEmpty = 1;
		//cout << "文件不存在" << endl;
		ifs.close();
		return;
	}
	//文件清空
	char ch;
	ifs >> ch;
	if (ifs.eof()) {
		//cout << "文件为空" << endl;
		this->fileEmpty = 1;
		ifs.close();
		return;
	}
	//文件不为空
	this->fileEmpty = 0;
	ifs.putback(ch);//将上面读取的字符放回来
	string data;

	int Index = 0;
	while (ifs >> data) {
		//cout << Index << endl;
		//cout << data << endl;
		//10007,81.3125,10009,79.825,10002,78.5375,
		int pos = -1;
		int start = 0;
		vector<string>v_String;
		while (1) {
			pos = data.find(",", start);
			if (pos == -1) {
				break;
			}
			string temp = data.substr(start, pos-start);
			//cout << temp << endl;
			start = pos + 1;
			v_String.push_back(temp);
		}
		this->m_Record.insert(make_pair(Index, v_String));
		Index++;
	}
	ifs.close();
}
void SpeechManager::showRecord() {
	if (this->fileEmpty) cout << "文件为空或不存在" << endl;
	else {
		for (int i = 0; i < this->m_Record.size(); i++) {
			cout << "第" << i + 1 << "届"<<endl<<"冠军姓名：" << this->m_Record[i][0] << " 得分：" << this->m_Record[i][1]
				<< endl<<"亚军姓名：" << this->m_Record[i][2] << " 得分：" << this->m_Record[i][3]
				<< endl<<"季军编号：" << this->m_Record[i][4] << " 得分：" << this->m_Record[i][5]
				<< endl;
		}
		system("pause");
		system("cls");
	}
}
void SpeechManager::clearRecord() {
	cout << "确认清空？" << endl;
	cout << "1、确认" << endl;
	cout << "2、返回" << endl;

	int cmd = 0;
	cin >> cmd;
	if (cmd == 1) {
		ofstream ofs("speech.csv", ios::trunc);
		ofs.close();
		//初始化属性
		this->initSpeech();
		this->creatSpeaker();
		this->loadRecord();

		cout << "清空成功" << endl;
	}
	system("pause");
	system("cls");
}
