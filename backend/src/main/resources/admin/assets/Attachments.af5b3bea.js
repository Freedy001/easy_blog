import{d as e,o as t,c as s,F as a,K as l,b as o,e as c,p as n,a as i,v as r,R as d,r as u,G as m,h as p,I as f,u as y,B as g,i as h,q as v,s as k,D as x,x as C}from"./vendor.7ce22028.js";import{g as _,a as b,p as E}from"./http.c8a04791.js";import{_ as I}from"./FullScreen.6c2fcf19.js";import"./index.455f64ef.js";n("data-v-5de269b2");const w={class:"container"},B={key:0,class:"loader"},N={key:1,style:{margin:"auto",color:"#909090"}};i();var S=e({expose:[],props:["hasMore"],setup:e=>(n,i)=>(t(),s("div",w,[e.hasMore?(t(),s("div",B,[(t(),s(a,null,l(5,(e=>o("div",{class:"dot",key:e}))),64))])):c("",!0),e.hasMore?c("",!0):(t(),s("div",N,"没有更多了😭😭~~"))]))});S.__scopeId="data-v-5de269b2";const L=v();n("data-v-4be7396f");const j={class:"root"},z={class:"btn-area"},H=k("上传"),M=k("删除"),T={class:"img-area"},$={class:"checked"},q=o("i",{class:"el-icon-upload"},null,-1),A=o("div",{class:"el-upload__text"},[k("将文件拖到此处，或"),o("em",null,"点击上传")],-1);i();var D=e({expose:[],setup(n){const{proxy:i}=C();let v;e({LoadMore:S,FullScreen:I});let k=(new Date).getTime();r((()=>{P().then((()=>{setTimeout((()=>{R();const e=document.querySelector("#content");window.onresize=()=>R(),e.onscroll=e=>{(new Date).getTime()-k>50&&B(e)}}),500),v=setInterval((()=>{R()}),1e3)}))})),d((()=>{clearInterval(v)}));let w=u(!0),B=({srcElement:e})=>{e.scrollTop+e.clientHeight>e.scrollHeight-50&&w.value&&(K++,P())},N=u(!1),D={Authorization:localStorage.getItem("Authorization")};async function F(e){200!=e.code?x({showClose:!0,message:`上传失败${e.msg}`,type:"error"}):(O.length=0,K=1,P().then())}let W=[];async function G(){const e=document.getElementsByClassName("pic-item");let t=[];W.forEach((s=>t.push(e[s].getAttribute("src"))));const s=await E("/file/delPic",t);if(200==s.code){i.$notify({title:"成功！",message:"删除成功"});const e=document.getElementsByClassName("item"),t=document.getElementsByClassName("checked");W.forEach((s=>{e[s].classList.remove("select"),t[s].style.display="none"})),W=[],O.length=0,K=1,P().then()}else i.$notify.error({title:"出差啦😢！",message:s.msg,duration:5e3})}let K=1,O=m([]);async function P(){const e=await _(`/file/getImages?page=${K}&limit=30&sidx=id&order=desc`);if(200==e.code){const t=e.data.list;0==t.length&&(w.value=!1),t.forEach(((e,t)=>{O.push(e.resourceUrl)}))}}function R(){let e=document.getElementsByClassName("item");let t=document.querySelector("#content").clientWidth-50,s=e[0].offsetWidth,a=parseInt(String(t/(s+10))),l=[];for(let o=0;o<e.length;o++)if(o<a)e[o].style.top=0,e[o].style.left=(s+10)*o+"px",l.push(e[o].offsetHeight);else{let t=l[0],s=0;for(let e=0;e<l.length;e++)t>l[e]&&(t=l[e],s=e);e[o].style.top=l[s]+10+"px",e[o].style.left=e[s].offsetLeft+"px",l[s]=l[s]+e[o].offsetHeight+10}}return(e,n)=>{const i=p("el-button"),r=p("el-image"),d=p("el-upload");return t(),s("div",j,[o("div",z,[o(i,{type:"primary",onClick:n[1]||(n[1]=e=>f(N)?N.value=!0:N=!0)},{default:L((()=>[H])),_:1}),o(i,{type:"danger",onClick:G},{default:L((()=>[M])),_:1})]),o("div",T,[(t(!0),s(a,null,l(y(O),((e,a)=>(t(),s("div",{class:"item",onClick:e=>function(e,t){const s=document.getElementsByClassName("item")[t],a=document.getElementsByClassName("checked")[t];let l;-1==(l=W.indexOf(t))?(W.push(t),s.classList.add("select"),a.style.display="block"):(W.splice(l,1),s.classList.remove("select"),a.style.display="none")}(0,a),key:a},[o("div",$,[o("img",{src:y("/admin/assets/check.a3d3306e.svg"),alt:""},null,8,["src"])]),o(r,{class:"pic-item",key:e,src:y(b)(e),lazy:""},null,8,["src"])],8,["onClick"])))),128))]),o(h,{name:"el-fade-in-linear"},{default:L((()=>[y(N)?(t(),s(I,{key:0,opacity:.5,index:3e3,onClick:n[3]||(n[3]=e=>f(N)?N.value=!1:N=!1)},{default:L((()=>[o(d,{onClick:n[2]||(n[2]=g((()=>{}),["stop"])),class:"upload-demo",drag:"",action:y(b)("/backend/file/upload"),"list-type":"picture",headers:y(D),"on-success":F,multiple:""},{default:L((()=>[q,A])),_:1},8,["action","headers"])])),_:1},8,["opacity"])):c("",!0)])),_:1})])}}});D.__scopeId="data-v-4be7396f";export default D;
