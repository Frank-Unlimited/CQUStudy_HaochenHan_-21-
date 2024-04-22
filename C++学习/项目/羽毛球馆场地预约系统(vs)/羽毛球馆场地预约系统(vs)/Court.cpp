#include"Court.h"

Court::Court() {
	ifstream ifs("Badminton Court.txt", ios::in);
	for (int i = 1; i <= 7; i++) {
		for (int j = 1; j <= 6; j++) {
			for (int k = 0; k < 24; k++) {
				court_status[i][j][k] = 0;
			}
		}
	}
	for (int i = 1; i <= 7; i++) {
		for (int j = 1; j <= 6; j++) {
			for (int k = 0; k < 24; k++) {
				ifs >> this->court_status[i][j][k];
			}
		}
	}
	ifs.close();
}

void Court::updateFile() {
	ofstream ofs("Badminton Court.txt", ios::out | ios::trunc);
	for (int i = 1; i <= 7; i++) {
		for (int j = 1; j <= 6; j++) {
			for (int k = 0; k < 24; k++) {
				ofs << this->court_status[i][j][k] << " ";
			}
			ofs << endl;
		}
		ofs << endl << endl;
	}
	ofs.close();
	cout << "===========������Ϣ�Ѹ��£�===========" << endl;
}

void Court::showCourt() {
	for (int i = 1; i <= 7; i++) {
		cout << "����" << i << endl;
		cout << "ʱ�䣺";
		for (int i = 0; i < 24; i++) cout << i << (i >= 10 ? " " : "  ");
		cout << endl;
		for (int j = 1; j <= 6; j++) {
			cout << "����" << j << ":";
			for (int k = 0; k < 24; k++) {
				cout << setiosflags(ios::left) << setw(2) << this->court_status[i][j][k] << " ";
			}
			cout << endl;
		}
		cout << endl << endl;
	}
}