#include<bits/stdc++.h>
using namespace std;

int main(){
	stack<int> s;
	if(s.empty()) cout<<"s is empty"<<endl;
	s.push(1);
	cout<<s.top()<<endl;
	s.pop();
	if(s.empty()) cout<<"s is empty"<<endl;
	queue<int> q;
	q.push(1);
	q.push(2);
	q.front()=0;
	q.pop();
	cout<<q.front()<<endl;
}
