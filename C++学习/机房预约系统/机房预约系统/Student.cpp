#include"Student.h"

//Ĭ�Ϲ���
Student::Student() {

}
//�вι���
Student::Student(int id, string name, string pwd) {
	//��ʼ��ѧ����Ϣ
	this->m_Id = id;
	this->m_Name = name;
	this->m_Pwd = pwd;
	//��ʼ��������Ϣ
	ifstream ifs(COMPUTER_FILE, ios::in);
	ComputerRoom c;
	while (ifs >> c.m_ComId && ifs >> c.m_MaxNum) {
		vCom.push_back(c);
	}
	ifs.close();

}
//�˵�����
void Student::operMenu() {
	cout << "��ӭѧ����" << this->m_Name << "��¼��" << endl;
	cout << endl;
	cout << "1.����ԤԼ" << endl;
	cout << "2.�鿴�ҵ�ԤԼ" << endl;
	cout << "3.�鿴����ԤԼ" << endl;
	cout << "4.ȡ��ԤԼ" << endl;
	cout << "0.ע����¼" << endl;
	cout << endl;
	cout << "����������ѡ��" << endl;
}
//����ԤԼ
void Student::applyOrder() {
	cout << "��������ʱ��Ϊ��һ������" << endl;
	cout << "����������ԤԼ��ʱ�䣺" << endl;
	cout << "1����һ" << endl;
	cout << "2���ܶ�" << endl;
	cout << "3������" << endl;
	cout << "4������" << endl;
	cout << "5������" << endl;
	
	//���ڽ����û���Ϣ
	int date = 0;//����
	int interval = 0;//ʱ���
	int room = 0;//�������

	while (1) {
		cin >> date;
		if (date >= 1 && date <= 5) break;
		cout << "������������������" << endl;
	}

	cout << "����������ʱ��Σ�" << endl;
	cout << "1������" << endl;
	cout << "2������" << endl;

	while (1) {
		cin >> interval;
		if (interval >= 1 && interval <= 2) break;
		cout << "������������������" << endl;
	}

	cout << "��ѡ�������" << endl;
	for (int i = 0; i < vCom.size(); i++) {
		cout << vCom[i].m_ComId << "�Ż��� ������" << vCom[i].m_MaxNum << endl;
	}

	while (1) {
		cin >> room;
		if (room >= 1 && room <= vCom.size()) break;
		cout << "������������������" << endl;
	}

	ofstream ofs;
	ofs.open(ORDER_FILE, ios::app);

	ofs << "date:" << date << " ";
	ofs << "interval:" << interval << " ";
	ofs << "studentId:" << this->m_Id << " ";
	ofs << "studentName:" << this->m_Name << " ";
	ofs << "roomId:" << room << " ";
	ofs << "status:" << 1 << endl;

	cout << "ԤԼ�ɹ��������" << endl;
	
	system("pause");
	system("cls");
}
//�鿴����ԤԼ
void Student::showMyOrder() {
	OrderFile of;
	if (of.m_Size==0) {
		cout << "��ԤԼ��¼" << endl;
		system("pause");
		system("cls");
		return;
	}
	for (int i = 0; i < of.m_Size; i++) {
		if (this->m_Id == atoi(of.m_orderData[i]["studentId"].c_str())) {//�ҵ�����ԤԼ
			cout << "ԤԼ���ڣ�����" << of.m_orderData[i]["date"] << " ";
			cout << "ʱ��Σ�" << (of.m_orderData[i]["interval"] == "1" ? "����" : "����") << " ";
			cout << "�����ţ�" << of.m_orderData[i]["roomId"] << " ";
			cout << "ԤԼ״̬��";
			if(of.m_orderData[i]["status"]=="1") cout << "�����" << " ";
			else if(of.m_orderData[i]["status"] == "2") cout << "ԤԼ�ɹ�" << " ";
			else if (of.m_orderData[i]["status"] == "-1") cout << "���δͨ��" << " ";
			else cout << "ԤԼ��ȡ��" << " ";
			cout << endl;
		}
	}
	system("pause");
	system("cls");
}
//�鿴����ԤԼ
void Student::showAllOrder() {
	OrderFile of;
	
	if (of.m_Size == 0) {
		cout << "��ԤԼ��¼" << endl;
		system("pause");
		return;
	}
	for (int i = 0; i < of.m_Size; i++) {
		cout << i + 1 << ", ";
		cout << "ԤԼ���ڣ� ��" << of.m_orderData[i]["date"] << " ";
		cout << "ʱ��Σ�" << (of.m_orderData[i]["interval"]=="1"?"����":"����") << " ";
		cout << "ѧ�ţ�" << setiosflags(ios::left) << setw(12) << of.m_orderData[i]["studentId"] << " ";
		cout << "������" << setw(6) << of.m_orderData[i]["studentName"] << " ";
		cout << "������ţ�" << of.m_orderData[i]["roomId"] << " ";
		cout << "���״̬��";
		if (of.m_orderData[i]["status"] == "1") cout << "�����" << " ";
		else if (of.m_orderData[i]["status"] == "2") cout << "ԤԼ�ɹ�" << " ";
		else if (of.m_orderData[i]["status"] == "-1") cout << "���δͨ��" << " ";
		else if (of.m_orderData[i]["status"] == "0") cout << "ԤԼ��ȡ��" << " ";
		cout << endl;
	}

	system("pause");
	system("cls");
}
//ȡ��ԤԼ
void Student::cancelOrder() {
	OrderFile of;

	if (of.m_Size == 0) {
		cout << "��ԤԼ��¼" << endl;
		system("pause");
		system("cls");
		return;
	}

	int index = 1;
	vector<int> v;//����±���
	for (int i = 0; i < of.m_Size; i++) {
		//�ж�������ѧ��
		if (this->m_Id == atoi(of.m_orderData[i]["studentId"].c_str())) {
			//ɸѡ״̬(�����/ԤԼ�ɹ�)
			if (of.m_orderData[i]["status"] == "1" || of.m_orderData[i]["status"] == "2") {
				v.push_back(i);
				cout << index++ << ", " << "ԤԼ���ڣ�����" << of.m_orderData[i]["date"] << " ";
				cout << "ʱ��Σ�" << (of.m_orderData[i]["interval"] == "1" ? "����" : "����") << " ";
				cout << "������ţ�" << of.m_orderData[i]["roomId"] << " ";
				cout << "���״̬��" << (of.m_orderData[i]["status"] == "1" ? "�����" : "ԤԼ�ɹ�") << endl;
			}
		}
	}

		cout << endl<<"����л�ԤԼ�ɹ��ļ�¼����ȡ����������ȡ���ļ�¼(����0����)" << endl;
		int cmd = 0;
		while (1) {
			cin >> cmd;
			if (cmd <= v.size() && cmd >= 0) {
				if (cmd == 0) {
					break;
				}
				else {
					of.m_orderData[v[cmd - 1]]["status"] = "0";
					of.updateOrder();//����������д
					cout << "��ȡ��ԤԼ" << endl;
					break;
				}
			}
			cout << "������������������" << endl;
		}

	system("pause");
	system("cls");
}