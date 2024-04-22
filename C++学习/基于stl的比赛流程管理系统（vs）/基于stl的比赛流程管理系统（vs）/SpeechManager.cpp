#include "SpeechManager.h"


SpeechManager::SpeechManager() {
	//��ʼ������
	this->initSpeech();
	//����ѡ��
	this->creatSpeaker();
	//���������¼
	this->loadRecord();
}
SpeechManager::~SpeechManager() {
	
}
void SpeechManager::show_Menu() {
	cout << "------------------------------------" << endl;
	cout << "��ӭ�μ������ѧ2022��7����ë�����" << endl;
	cout << "1. ��ʼ��ë�����" << endl;
	cout << "2. �鿴�����¼" << endl;
	cout << "3. ��ձ�����¼" << endl;
	cout << "0. �˳���������" << endl;
	cout << "------------------------------------" << endl;
	cout << endl;
}
void SpeechManager::exitSystem() {
	cout << "��ӭ�´�ʹ��" << endl;
	system("pause");
	exit(0);
}
void SpeechManager::initSpeech() {
	//�����ƿ�
	this->m_Record.clear();
	this->v1.clear();
	this->v2.clear();
	this->vVictory.clear();
	this->m_Speaker.clear();
	//����������ʼ��
	this->m_Index = 1;
}
void SpeechManager::creatSpeaker() {
	string nameSeed[12] = { "��꿺�","��껳�","������","����","���","������"
		,"�Ͽ���","�ֵ�","�շƿ�","����ΰ","��ѩ��","�ǵ�" };
	for (int i = 0; i < 12; i++) {
		Speaker sp;
		sp.name = "ѡ��";
		sp.name += nameSeed[i];//����ѡ������
		FOR(2) sp.score[i] = 0;//��ʼ��ѡ�ֵ÷�
		this->v1.push_back(i + 10001);//����ѡ�ֱ�ţ�����v1
		this->m_Speaker.insert(make_pair(i + 10001, sp));
	}
}
void SpeechManager::speechDraw() {
	if (this->m_Index == 1) {
		cout << "�ڡ�" << m_Index << "���ֱ������ڳ�ǩ" << endl;
		random_shuffle(v1.begin(), v1.end());//����˳��
		cout << "------------------------------------" << endl;
		cout << "��ǩ������£�" << endl;
		for (vector<int>::iterator it = v1.begin(); it != v1.end(); it++) {
			cout << *it << " "<<this->m_Speaker[*it].name<<endl;
		}
		cout << endl;
	}
	else {
		cout << "�ڡ�" << m_Index << "���ֱ������ڳ�ǩ" << endl;
		random_shuffle(v2.begin(), v2.end());
		cout << "------------------------------------" << endl;
		cout << "��ǩ������£�" << endl;
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
	cout << "----------------�ڡ�" << this->m_Index << "���ֱ�����ʽ��ʼ-----------------" << endl;
	vector<int>v_Src;//����ѡ������
	if (this->m_Index == 1) v_Src = v1;
	else v_Src = v2;

	int num = 0;//��¼�Ѿ������˵�ѡ������
	multimap<double, int, greater<double>> groupScpre;//��ʱ�������С��ɼ�

	for (vector<int>::iterator it = v_Src.begin(); it != v_Src.end(); it++) {//ÿ��ѡ�����α���
		num++;
		//��ί���
		deque<double>d;
		FOR(10) {
			double score = (rand() % 400 + 600) / 10.f;//600~1000
			//cout << score << " ";
			d.push_back(score);
		}
		//cout << endl;
		//��d���в����õ�ƽ���֣�
		sort(d.begin(), d.end(), greater<double>());
		d.pop_back();
		d.pop_front();
		double sum = accumulate(d.begin(), d.end(), 0.0f);
		double avg = sum / (double)d.size();
		//cout << "��ţ�" << *it << " ������" << this->m_Speaker[*it].name << " ƽ���֣�" << avg << endl;

		//*it��ѡ�ֱ��
		m_Speaker[*it]/*ȡ����Ŷ�Ӧ��Speaker*/.score[this->m_Index - 1] = avg;//��avg��ֵ����Ӧѡ��
		groupScpre.insert(make_pair(avg, *it));
		if (num % 6 == 0) {
			cout << "�ڡ�" << num / 6 << "����������Σ�" << endl;
			for (multimap<double, int, greater<double>>::iterator it = groupScpre.begin(); it != groupScpre.end(); it++) {
				cout << "��ţ�" << it->second << " ������" << m_Speaker[it->second].name << " ƽ���֣�"
					<< m_Speaker[it->second].score[this->m_Index - 1] << endl;
			}
			//ȡ��ǰ����
			int count = 0;
			for (multimap<double, int, greater<double>>::iterator it = groupScpre.begin(); it != groupScpre.end() && count < 3; count++, it++) {
				if (this->m_Index == 1) this->v2.push_back((*it).second);
				else this->vVictory.push_back((*it).second);
			}
			groupScpre.clear();
			cout << endl;
		}
	}
	cout << "----------------�ڡ�" << this->m_Index << "���ֱ�������-----------------" << endl;
	system("pause");
}
void SpeechManager::showScore() {
	cout << "-----------�ڡ�" << this->m_Index << "���ֱ�������ѡ����Ϣ���£�" << endl;
	vector<int>v;
	//ѡ��v������������
	if (this->m_Index == 1) v = v2;
	else v = vVictory;
	for (vector<int>::iterator it = v.begin();it != v.end(); it++) {
		cout << "��ţ�" << *it << " ������" << this->m_Speaker[*it].name << " ƽ���֣�"
			<< this->m_Speaker[*it].score[m_Index - 1] << endl;
	}
	system("pause");
	system("cls");
	this->show_Menu();
}
void SpeechManager::saveRecord() {
	ofstream ofs;
	ofs.open("speech.csv", ios::out | ios::app);//׷�ӵķ�ʽд�ļ�
	for (vector<int>::iterator it = vVictory.begin(); it != vVictory.end(); it++) {
		ofs << m_Speaker[*it].name<< "," << m_Speaker[*it].score[1] << ",";
	}
	ofs << endl;
	ofs.close();
	cout << "��¼�Ѿ�����" << endl;
	this->fileEmpty = 0;
}
void SpeechManager::startSpeech() {
	//��һ�ֱ���
	//��ǩ
	this->speechDraw();
	//����
	this->speechContest();
	//��ʾ���
	this->showScore();
	//�ڶ��ֱ���
	this->m_Index++;
	//��ǩ
	this->speechDraw();
	//����
	this->speechContest();
	//��ʾ���
	this->showScore();
	//�������
	this->saveRecord();
	//���ñ�������ȡ��¼
	this->initSpeech();
	this->creatSpeaker();
	this->loadRecord();

	cout << "����������" << endl;

	system("pause");
	system("cls");
}
void SpeechManager::loadRecord() {
	ifstream ifs("speech.csv", ios::in);//���ļ�
	if (!ifs.is_open()) {
		this->fileEmpty = 1;
		//cout << "�ļ�������" << endl;
		ifs.close();
		return;
	}
	//�ļ����
	char ch;
	ifs >> ch;
	if (ifs.eof()) {
		//cout << "�ļ�Ϊ��" << endl;
		this->fileEmpty = 1;
		ifs.close();
		return;
	}
	//�ļ���Ϊ��
	this->fileEmpty = 0;
	ifs.putback(ch);//�������ȡ���ַ��Ż���
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
	if (this->fileEmpty) cout << "�ļ�Ϊ�ջ򲻴���" << endl;
	else {
		for (int i = 0; i < this->m_Record.size(); i++) {
			cout << "��" << i + 1 << "��"<<endl<<"�ھ�������" << this->m_Record[i][0] << " �÷֣�" << this->m_Record[i][1]
				<< endl<<"�Ǿ�������" << this->m_Record[i][2] << " �÷֣�" << this->m_Record[i][3]
				<< endl<<"������ţ�" << this->m_Record[i][4] << " �÷֣�" << this->m_Record[i][5]
				<< endl;
		}
		system("pause");
		system("cls");
	}
}
void SpeechManager::clearRecord() {
	cout << "ȷ����գ�" << endl;
	cout << "1��ȷ��" << endl;
	cout << "2������" << endl;

	int cmd = 0;
	cin >> cmd;
	if (cmd == 1) {
		ofstream ofs("speech.csv", ios::trunc);
		ofs.close();
		//��ʼ������
		this->initSpeech();
		this->creatSpeaker();
		this->loadRecord();

		cout << "��ճɹ�" << endl;
	}
	system("pause");
	system("cls");
}
