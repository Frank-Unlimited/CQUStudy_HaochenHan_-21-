#include"orderFile.h"

//拆分字符串生成map
pair<string, string> Substr(string total) {
	//date : 1
	string key="error";
	string value="error";
	int pos = total.find(":");
	if (pos != -1) {
		key = total.substr(0, pos);
		value = total.substr(pos + 1, total.size() - pos - 1);
		//cout << key << endl << value << endl;
	}
	else cout << "字符串解析错误！" << endl;
	pair<string, string> m(key,value);
	return m;
}

//构造函数
OrderFile::OrderFile() {
	ifstream ifs;
	ifs.open(ORDER_FILE, ios::in);

	string date;
	string interval;
	string studentId;
	string studentName;
	string roomId;
	string status;

	this->m_Size = 0;
	
	while (ifs >> date && ifs >> interval && ifs >> studentId 
		&& ifs >> studentName && ifs >> roomId && ifs >> status) {
		map<string, string> m;
		m.insert(Substr(date));
		m.insert(Substr(interval));
		m.insert(Substr(studentId));
		m.insert(Substr(studentName));
		m.insert(Substr(roomId));
		m.insert(Substr(status));

		//将小map放到大map中
		this->m_orderData.insert(make_pair(this->m_Size, m));
		this->m_Size++;
	}
	ifs.close();

	/*for (map<int, map<string, string>>::iterator it = m_orderData.begin(); it != m_orderData.end(); it++) {
		cout << "条数 = " << it->first << "  value = " << endl;
		for (map<string, string>::iterator mit = (*it).second.begin(); mit != (*it).second.end(); mit++) {
			cout << "key = " << mit->first << "  value = " << mit->second << " ";
		}
		cout << endl;
	}*/
}

//更新预约记录
void OrderFile::updateOrder() {
	if (this->m_Size == 0) return;
	ofstream ofs(ORDER_FILE, ios::out | ios::trunc);
	for (int i = 0; i < m_Size; i++) {
		ofs << "date:" << this->m_orderData[i]["date"] << " ";
		ofs << "interval:" << this->m_orderData[i]["interval"] << " ";
		ofs << "studentId:" << this->m_orderData[i]["studentId"] << " ";
		ofs << "studentName:" << this->m_orderData[i]["studentName"] << " ";
		ofs << "roomId:" << this->m_orderData[i]["roomId"] << " ";
		ofs << "status:" << this->m_orderData[i]["status"] << endl;
	}
	ofs.close();
}