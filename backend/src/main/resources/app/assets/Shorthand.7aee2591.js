import{_ as a}from"./UserInfo.9cbc9415.js";import{g as s,a as e}from"./index.c7fc0e38.js";import{d as t,r as n,g as o,f as r,o as l,c,b as i,F as d,n as u,x as m,u as v,p,a as f}from"./vendor.03637da4.js";p("data-v-734d4d5c");const h={class:"container"},g={class:"time"},k={class:"publishTime"};f();var y=t({expose:[],setup(t){let p=n(0),f=n(0),y=n();o((()=>{!async function(){const a=await s("/shorthand/list?page=1&limit=10");200==a.code&&(I.value=a.data.list)}()}));let I=n();r({});const b=document.getElementsByClassName("shorthand");return(s,t)=>(l(),c("div",{class:["root",v(e)()]},[i("div",h,[(l(!0),c(d,null,u(v(I),((a,s)=>(l(),c("div",{class:"item shorthand",onMouseenter:a=>{b[s].style.transform="scale(1.03)"},onMouseleave:a=>{b[s].style.transform="scale(1)"},key:a.createTime},[i("p",null,m(a.content),1),i("div",g,[i("a",{href:"javascript:void(0)",onClick:s=>{return e=a.managerId,t=s,p.value=t.clientX,f.value=t.clientY,void(y.value=e);var e,t},class:"nickName"},m(a.nickname),9,["onClick"]),i("span",k,m(a.publishTime),1)])],40,["onMouseenter","onMouseleave"])))),128))]),i(a,{startX:v(p),startY:v(f),userId:v(y)},null,8,["startX","startY","userId"])],2))}});y.__scopeId="data-v-734d4d5c";export default y;
