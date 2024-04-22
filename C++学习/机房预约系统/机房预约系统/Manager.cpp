#include"Manager.h"

//Ĭ�Ϲ���
Manager::Manager() {

}
//�вι���
Manager::Manager(string name, string psw) {
	//��ʼ������Ա��Ϣ
	this->m_Name = name;
	this->m_Pwd = psw;
	//��ʼ������,��ȡ��ʦ��ѧ����Ϣ
	this->initVector();
	//��ʼ��������Ϣ
	this->initComputer();
}
//����˺�
void Manager::addPerson() {
	cout << "����������˺ŵ����ͣ�" << endl;
	cout << "1�����ѧ��" << endl;
	cout << "2�������ʦ" << endl;

	string fileName;//�����ļ���
	string tip;//��ʾid
	string errorTip;//�ظ�������ʾ

	ofstream ofs;
	
	int cmd = 0;
	cin >> cmd;
	if (cmd == 1) {//���ѧ��
		fileName = STUDENT_FILE;
		tip = "������ѧ�ţ�";
		errorTip = "ѧ���ظ������������루����-1�˳���";
	}
	else {//�����ʦ
		fileName = TEACHER_FILE;
		tip = "������ְ���ţ�";
		errorTip = "ְ�����ظ������������루����-1�˳���";
	}
	ofs.open(fileName, ios::out | ios::app);

	int id;//ѧ��/ְ����
	string name;
	string pwd;

	cout << tip << endl;
	while (1) {
		cin >> id;
		if (id == -1) {
			ofs.close();
			system("cls");
			return;
		}
		if (this->checkRepeat(id, cmd)) {
			cout << errorTip << endl;
		}
		else {
			break;
		}
	}

	cout << "������������" << endl;
	cin >> name;

	cout << "���������룺" << endl;
	cin >> pwd;

	//���ļ����������
	ofs << id << " " << name << " " << pwd << " " << endl;
	cout << "��ӳɹ�";

	system("pause");
	system("cls");

	ofs.close();
	
	//���»�ȡ�ļ�������
	this->initVector();
}
//���ԤԼ��¼
void Manager::clearPerson() {
	ofstream ofs(ORDER_FILE, ios::trunc);
	ofs.close();

	cout << "��ճɹ���" << endl;
	system("pause");
	system("cls");
}
//�鿴������Ϣ
void Manager::showComputer() {
	cout << "������Ϣ���£�" << endl;
	for (vector<ComputerRoom>::iterator it = vCom.begin(); it != vCom.end(); it++) {
		cout << "������ţ�" << it->m_ComId << " ���������" << it->m_MaxNum << endl;
	}
	system("pause");
	system("cls");
}

void printStu(Student& s) {
	cout << "ѧ�ţ�" << s.m_Id << " ������" << s.m_Name << " ���룺" << s.m_Pwd << endl;
}
void printTea(Teacher& t) {
	cout << "ְ���ţ�" << t.empId << " ������" << t.m_Name << " ���룺" << t.m_Pwd << endl;
}
//�鿴�˺�
void Manager::showPerson() {
	cout << "ѡ��鿴�����ݣ�" << endl;
	cout << "1���鿴����ѧ��" << endl;
	cout << "2���鿴������ʦ" << endl;

	int cmd = 0;
	cin >> cmd;
	if (cmd == 1) {
		//�鿴ѧ��
		for_each(vStu.begin(), vStu.end(), printStu);
	}
	else {
		//�鿴��ʦ
		for_each(vTea.begin(), vTea.end(), printTea);
	}
	system("pause");
	system("cls");
}
//�˵�����
void Manager::operMenu() {
	cout << "��ӭ����Ա��" << this->m_Name << "��¼��" << endl;
	cout << endl;
	cout << "1.����˺�" << endl;
	cout << "2.�鿴�˺�" << endl;
	cout << "3.�鿴����" << endl;
	cout << "4.���ԤԼ" << endl;
	cout << "0.ע����¼" << endl;
	cout << endl;
	cout << "����������ѡ��" << endl;
}
//��ʼ������
void Manager::initVector() {
	//�������
	this->vStu.clear();
	this->vTea.clear();
	//��ȡ��Ϣ
	ifstream ifs;
	ifs.open(STUDENT_FILE, ios::in);
	if (!ifs.is_open()) {
		cout << "�ļ���ȡʧ�ܣ�" << endl;
		return;
	}
	Student s;
	while (ifs >> s.m_Id && ifs >> s.m_Name && ifs >> s.m_Pwd) {
		vStu.push_back(s);
	}
	//cout << "ѧ��������" << vStu.size() << endl;
	ifs.close();

	ifs.open(TEACHER_FILE, ios::in);
	if (!ifs.is_open()) {
		cout << "�ļ���ȡʧ�ܣ�" << endl;
		return;
	}
	Teacher t;
	while (ifs >> t.empId && ifs >> t.m_Name && ifs >> t.m_Pwd) {
		vTea.push_back(t);
	}
	//cout << "��ʦ������" << vTea.size() << endl;
	ifs.close();
}
//����ظ�
bool Manager::checkRepeat(int id/*���ѧ��/ְ����*/, int type/*�������*/) {
	if (type == 1) {//���ѧ��
		for (vector<Student>::iterator it = vStu.begin(); it != vStu.end(); it++) {
			if (id == it->m_Id) {
				return 1;
			}
		}
	}
	else {//�����ʦ
		for (vector<Teacher>::iterator it = vTea.begin(); it != vTea.end(); it++) {
			if (id == it->empId) {
				return 1;
			}
		}
	}
	return 0;
}
//��ʼ������
void Manager::initComputer() {
	ifstream ifs;
	ifs.open(COMPUTER_FILE, ios::in);
	if (!ifs.is_open()) {
		cout << "�ļ���ȡʧ�ܣ�" << endl;
		return;
	}
	ComputerRoom c;
	while (ifs >> c.m_ComId && ifs >> c.m_MaxNum) {
		this->vCom.push_back(c);
	}
	ifs.close();
	//cout << "����������" << vCom.size();
}