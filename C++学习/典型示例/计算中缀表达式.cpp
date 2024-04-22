#include<bits/stdc++.h>
#define FOR(n) for(int i=0;i<n;i++)
#define INF 0x3f3f3f3f
using namespace std;

class Solution {
public:
    int evalRPN(vector<string>& tokens) {
        stack<int> s;
        for (auto i : tokens) {
            if (!s.empty()){}

//                cout << s.top() << endl;
            if (i == "*") {
                int a = s.top();
                s.pop();
                int b = s.top();
                s.pop();
                s.push(a * b);
            } else if (i == "/") {
                int a = s.top();
                s.pop();
                int b = s.top();
                s.pop();
                s.push(b / a);
            } else if (i == "+") {
                int a = s.top();
                s.pop();
                int b = s.top();
                s.pop();
                s.push(a + b);
            } else if (i == "-") {
                int a = s.top();
                s.pop();
                int b = s.top();
                s.pop();
                s.push(b - a);
            } else {
                s.push(stoi(i));
            }
        }
        return s.top();
    }
    bool isCaoZuoFu(char c) {
        return c == '+' || c == '-' || c == '(' || c == ')' || c == '*' ||
               c == '/';
    }
    vector<string> str2vec(string str) {
        string cur = "";
        vector<string> ans;
        int n = str.length();
        string pre = "start";
        bool flag = false;
        for (int i = 0; i < n; i++) {
//        	cout<<str[i]<<'|';
            if (str[i] == ' ') {
            } else if (str[i] == '-' && (pre == "start" || pre == "(")) {
                pre = "-";
                ans.push_back("0");
                ans.push_back("-");
                flag = true;
            } else if (str[i] == '-' &&
                       (pre == "+" || pre == "-" || pre == "*" || pre == "/")) {
                cur += str[i];
                pre = str[i];
                flag = true;
            } else if (isCaoZuoFu(str[i])) {
                ans.push_back(cur);
                cur = "";
                string tmp(1, str[i]);
                ans.push_back(tmp);
                pre = str[i];
                flag = true;
            } else {
                cur += str[i];
                pre = str[i];
                flag = true;
            }
            if (i == n - 1)
                ans.push_back(cur);
        }

        return ans;
    }
    int calculate(string s) {
        vector<string> v = this->str2vec(s);
        cout << "分割运算式：" << endl;
        for (auto i : v)
            cout << i << ' ';
        cout << endl<<endl;
        vector<string> nbl;
        stack<string> st;
        for (string i : v) {
            if (i == "" || i == " ")
                continue;
            // cout << i << endl;
            if (i == "+" || i == "-") {
                while (!st.empty() && st.top() != "(" &&
                       (st.top() == "*" || st.top() == "/" || st.top() == "-" ||
                        st.top() == "+")) {
                    nbl.push_back(st.top());
                    st.pop();
                }
                st.push(i);
            } else if (i == "*" || i == "/") {
                while (!st.empty() && st.top() != "(" &&
                       (st.top() == "*" || st.top() == "/")) {
                    nbl.push_back(st.top());
                    st.pop();
                }
                st.push(i);
            } else if (i == "(") {
                st.push(i);
            } else if (i == ")") {
                while (!st.empty() && st.top() != "(") {
                    nbl.push_back(st.top());
                    st.pop();
                }
                if (st.top() == "(")
                    st.pop();
            } else {
                nbl.push_back(i);
            }
        }
        while (!st.empty()) {
            nbl.push_back(st.top());
            st.pop();
        }

		cout<<"得到逆波兰式："<<endl;
         for (auto i : nbl)
             cout << i << ' ';
         cout << endl<<endl;
        return this->evalRPN(nbl);
    }
};

int main(){
	string input="-       9";
	Solution s;
	while(input!="exit"){
		cout<<"输入运算表达式："<<endl;
		cin>>input;
		int res = s.calculate(input);
		cout<<"结果："<<endl;
		cout<<res<<endl;
		system("pause");
		system("cls");

	}
}
