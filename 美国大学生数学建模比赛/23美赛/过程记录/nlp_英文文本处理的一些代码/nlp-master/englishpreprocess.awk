#!/bin/gawk
BEGIN{
readBegin();
readAcronym();
}
{

}
END{

}
function readBegin(){

}
function readAcronym(){

}
function tokenWord(word){
  if ( word ~ "-" ){
    nstr=split(word,str,"-");
    for (i=1;i<=nstr;i++){
      if ( str[i] 
  }
  if ( word ~ /^[a-z]/ ){
    
  }
}
