#include"Teacher.h"

//Ĭ�Ϲ���
Teacher::Teacher() {

}
//�вι���
Teacher::Teacher(int empId, string name, string psw) {
	this->empId = empId;
	this->m_Name = name;
	this->m_Pwd = psw;
}
//�鿴����ԤԼ
void Teacher::showAllOrder() {
	OrderFile of;
	if (of.m_Size == 0) {
		cout << "��ԤԼ��¼" << endl;
		system("pause");
		return;
	}
	for (int i = 0; i < of.m_Size; i++) {
		cout << i + 1 << ", ";
		cout << "ԤԼ���ڣ� ��" << of.m_orderData[i]["date"] << " ";
		cout << "ʱ��Σ�" << (of.m_orderData[i]["interval"] == "1" ? "����" : "����") << " ";
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
//�˵�����
void Teacher::operMenu() {
	cout << "��ӭ��ʦ��" << this->m_Name << "��¼��" << endl;
	cout << endl;
	cout << "1.�鿴����ԤԼ" << endl;
	cout << "2.���ԤԼ" << endl;
	cout << "0.ע����¼" << endl;
	cout << endl;
	cout << "����������ѡ��" << endl;
}
//���ԤԼ
void Teacher::validOrder() {
	OrderFile of;

	if (of.m_Size == 0) {
		cout << "��ԤԼ��¼" << endl;
		system("pause");
		system("cls");
		return;
	}
	int index = 1;
	vector<int> v;//����±���
	cout << "����˵�ԤԼ��¼���£�" << endl;
	for (int i = 0; i < of.m_Size; i++) {
		if (of.m_orderData[i]["status"] == "1") {
			v.push_back(i);
			cout << index++ << ", " << "ԤԼ���ڣ�����" << of.m_orderData[i]["date"] << " ";
			cout << "ʱ��Σ�" << (of.m_orderData[i]["interval"] == "1" ? "����" : "����") << " ";
			cout << "ѧ����ţ�" << setiosflags(ios::left)<<setw(8) << of.m_orderData[i]["studentId"] << " ";
			cout << "ѧ��������" << setiosflags(ios::left) << setw(6) << of.m_orderData[i]["studentName"] << " ";
			cout << "������ţ�" << of.m_orderData[i]["roomId"] << " ";
			cout << "���״̬�������" << endl;
		}
	}
	cout << endl << "��������˵�ԤԼ(����0����)" << endl;

	int cmd = 0;
	while (1) {
		cin >> cmd;
		if (cmd <= v.size() && cmd >= 0) {
			if (cmd == 0) break;
			else {
				cout << "��������˽��" << endl;
				cout << "1��ͨ��" << endl;
				cout << "2����ͨ��" << endl;
				int cmd2;
				while (1) {
					cin >> cmd2;
					if (cmd2 == 1) {
						of.m_orderData[v[cmd - 1]]["status"] = "2";
						break;
					}
					else if(cmd2 == 2){
						 of.m_orderData[v[cmd - 1]]["status"] = "-1";
						break;
					}
					else cout << "�����������������" << endl;
				}
				of.updateOrder();//����������д
				cout << "������" << endl;
				break;
			}
		}
		cout << "������������������" << endl;
	}
	
	system("pause");
	system("cls");
}