import{d as e,s as t,A as a,f as l,r as o,w as s,g as n,t as c,o as i,c as m,b as r,i as d,v as u,u as p,j as v,l as f,x as h,q as y,p as g,a as k,C,F as b,n as T,h as I,y as w,m as x,z as E,N,L,T as B,D as H,M,P as _}from"./vendor.03637da4.js";import{a as q,p as S,l as $,_ as A,i as j,g as U,c as D}from"./index.c7fc0e38.js";import{_ as V}from"./UserInfo.9cbc9415.js";import{_ as X}from"./LoadMore.d34531f5.js";const Y=y();g("data-v-8094f3a4");const z={class:"bottom"},F={class:"hint red"},P={style:{color:"#ff268b"}};k();var W=e({expose:[],props:["fatherCommentId","myplaceholder"],emits:["commentCB"],setup(e){const{proxy:y}=C(),g=t(),k=a();let b=l({articleId:"",username:"",email:"",content:""}),T=o(""),I=o(!1);async function w(){if(""==b.username||""==b.email||""===b.content)T.value="内容不能为空哦~~ 😥😥";else if(/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(b.email)){const e=g.query.id;if(e){const t={articleId:e,fatherCommentId:y.fatherCommentId,username:b.username,email:b.email,content:b.content};200==(await S("/comment/publish",t)).code&&(localStorage.setItem("username",b.username),localStorage.setItem("email",b.email),I.value=!0,b.content="",y.$emit("commentCB",t))}}else T.value="邮箱格式不正确~~ 😥😥"}return s(I,(e=>{e&&setTimeout((()=>{I.value=!1}),1500)})),n((()=>{b.username=localStorage.getItem("username"),b.email=localStorage.getItem("email")})),(t,a)=>{const l=c("el-popover");return i(),m("div",{class:["root",p(q)()],onClick:a[5]||(a[5]=e=>v(T)?T.value="":T="")},[r("div",{class:["input-box",p(q)()]},[d(r("input",{autocomplete:"off",id:"t1",type:"text","onUpdate:modelValue":a[1]||(a[1]=e=>p(b).username=e),placeholder:"Name"},null,512),[[u,p(b).username]]),d(r("input",{autocomplete:"off",id:"t2",type:"text","onUpdate:modelValue":a[2]||(a[2]=e=>p(b).email=e),placeholder:"Email"},null,512),[[u,p(b).email]])],2),d(r("textarea",{class:[p(q)(),"textarea"],placeholder:null==e.myplaceholder?"What do you want to say...":e.myplaceholder,"onUpdate:modelValue":a[3]||(a[3]=e=>p(b).content=e)},null,10,["placeholder"]),[[u,p(b).content]]),r("div",z,[r(l,{placement:"top",title:"success",trigger:"manual",content:p(k).state.indexSetting.examination?"评论成功请耐心等待管理员审核哦！":"评论发布成功😎!",visible:p(I),"onUpdate:visible":a[4]||(a[4]=e=>v(I)?I.value=e:I=e)},{reference:Y((()=>[r("button",{type:"button",class:{"el-button":!0,"shake-horizontal":""!==p(T),"button-dark":p(k).state.darkMode},onClick:f(w,["stop"])},"SUBMIT ",10,["onClick"])])),_:1},8,["content","visible"]),r("div",F,[r("span",P,h(p(T)),1)])])],2)}}});W.__scopeId="data-v-8094f3a4";const G=y();g("data-v-2ed782bc");const J={style:{transition:"all 0.5s ease"}},K={class:"container"},O={class:"header-img"},Q={class:"info"},R={class:"name"},Z={class:"content"},ee={class:"time"},te={class:"children-info"},ae={class:"content"},le={key:0},oe=w("回复 "),se={class:"replay-to"},ne={class:"time"};k();var ce=e({expose:[],props:["commentItem"],emits:["commentCB"],setup(t){e({Comment:W});const{proxy:a}=C();let l=o(!1);async function s(e){l.value=!1,setTimeout((()=>{a.$emit("commentCB",e)}),600)}let n,d=o(),u=o(),v=o(null);async function f(e,t){l.value=n!=e||!l.value,n=e,d.value=e,u.value="回复 @"+t+":",setTimeout((()=>{if(l.value){const e=v.value;window.scrollTo({top:e.offsetTop-e.clientHeight,behavior:"smooth"})}}),500)}return(e,a)=>{const o=c("el-divider");return i(),m("div",J,[r("div",K,[r("div",O,[r("img",{src:p($)(`/resource/Boy-0${(new Date).getTime()%5+1}.svg`),alt:""},null,8,["src"])]),r("div",Q,[r("span",R,h(t.commentItem.username),1),r("p",Z,h(t.commentItem.content),1),r("div",ee,[r("span",null,h(t.commentItem.createTime),1),r("span",{class:"replay",onClick:a[1]||(a[1]=e=>f(t.commentItem.id,t.commentItem.username))},"回复")])])]),(i(!0),m(b,null,T(t.commentItem.childComment,(e=>(i(),m("div",te,[r("div",ae,[r("p",null,[r("span",null,h(e.username)+":",1),null!==e.parentName&&e.parentName!==t.commentItem.username?(i(),m("span",le,[oe,r("span",se,"@"+h(e.parentName),1)])):I("",!0),w(" "+h(e.content),1)])]),r("div",ne,[r("span",null,h(e.createTime),1),r("span",{class:"replay",onClick:t=>f(e.id,e.username)},"回复",8,["onClick"])]),r(o)])))),256)),r(x,{"enter-active-class":"scale-in-tr","leave-active-class":"scale-out-left"},{default:G((()=>[r("div",{ref:v},[p(l)?(i(),m(W,{key:0,myplaceholder:p(u),onCommentCB:s,fatherCommentId:p(d)},null,8,["myplaceholder","fatherCommentId"])):I("",!0)],512)])),_:1}),r(o)])}}});ce.__scopeId="data-v-2ed782bc";const ie=y();g("data-v-5972e77d");const me={class:"root"},re={class:"title"},de={class:"img"},ue={class:"content"},pe={class:"block"},ve={class:"info"},fe={class:"item"},he={class:"item"},ye={class:"item"},ge={key:0},ke={key:0,class:"comment-header"},Ce=r("span",{id:"CommentList"},"Comment List",-1),be={key:1,class:"no-comment"},Te={key:1,class:"no-comment"};k();var Ie=e({expose:[],setup(c){const u=t();E();let v=u.query.id;e({Comment:W,CommentList:ce,LoadMore:X,UserInfo:V,FullScreenLoading:A}),s((()=>u.query.id),(()=>{K(),le=1,oe()})),o();let f=l({});let y,g=l({backgroundColor:"rgb(205,205,205)"}),k=o(!1),C=o(!1),w=!0;function S(){k.value=!0,g.backgroundColor="tomato"}function Y(){w&&(k.value=!1,g.backgroundColor="rgb(205,205,205)")}async function z(){if(y&&(clearTimeout(y),C.value=!1),u.query.id){200==(await U(`/article/likeArticle?id=${u.query.id}`)).code&&(F.likeNum++,localStorage.setItem(u.query.id+"","like"),w=!1,g.backgroundColor="tomato",C.value=!0,k.value=!0,y=setTimeout((()=>{C.value=!1}),2e3))}}n((()=>{localStorage.getItem(u.query.id+"")&&(w=!1,g.backgroundColor="tomato",k.value=!0)}));let F=l({});function P(){const e=document.getElementsByTagName("head")[0],t=document.createElement("link"),a=document.createElement("link");t.href=$(j()?"/resource/md-dark.css":"/resource/md.css"),t.setAttribute("rel","stylesheet"),t.setAttribute("class","md-css"),a.href=$(j()?"/resource/hj-dark.css":"/resource/hj.css"),a.setAttribute("rel","stylesheet"),a.setAttribute("class","md-css"),e.appendChild(t),e.appendChild(a)}n((async()=>{P(),K().then(),oe().then()}));const G=a();function J(){const e=document.getElementsByClassName("md-css"),t=e.length;for(let a=0;a<t;a++)e[0].remove()}async function K(){let e=u.query.id;if(e){const t=await U(`/article/get?id=${e}`);if(200===t.code){const e=t.data;D(e,F),setTimeout((()=>{const e=document.getElementsByClassName("comment-component"),t=document.getElementById("markdown").children,a=document.createElement("ul");let l=[];for(let n=0;n<t.length;n++){const e=t[n],o=e.tagName;if("H1"==o||"H2"==o||"H3"==o||"H4"==o||"H5"==o||"H6"==o){const t=document.createElement("li"),s=o.match(/\d/)[0],c=document.createElement("div");c.innerHTML=e.innerHTML,c.className="level"+s,t.appendChild(c),t.setAttribute("class","li-"+n),l[n]=e.offsetTop,t.addEventListener("click",(()=>{window.scrollTo({top:e.offsetTop,behavior:"smooth"}),ee=!1,R&&(R.style.color=""),t.style.color="#3eaf7c",setTimeout((()=>{ee=!0}),1e3)})),a.appendChild(t)}}const o=document.getElementById("toc");o.appendChild(a);let s=Date.now();document.body.onscroll=({srcElement:t})=>{const a=t.scrollingElement;if(Date.now()-s>1){try{a.scrollTop>280?Z.value=!0:Z.value=!1,0==F.articleComment&&a.scrollTop>e[0].offsetTop-a.clientHeight+300&&(Z.value=!1),a.scrollTop+a.clientHeight>a.scrollHeight-50&&O.value&&(Q.value=!0,le++,oe().then((()=>{Q.value=!O.value}))),l.forEach(((e,t)=>{if(e>a.scrollTop-50&&e<a.scrollTop+50&&ee){R&&(R.style.color="");const e=document.querySelector(`.li-${t}`);throw R=e,e.style.color="#3eaf7c",o.scrollTo({top:e.offsetTop-300,behavior:"smooth"}),new Error("LoopTerminates")}}))}catch(n){if("LoopTerminates"!==n.message)throw n}s=Date.now()}}}),500),setTimeout((()=>{L.highlightAll()}),100)}}}s((()=>G.state.darkMode),(()=>{J(),P()})),N(((e,t,a)=>{setTimeout((()=>{J()}),300),a()}));let O=o(!0),Q=o(!1);l({});let R,Z=o(!1),ee=!0;let te=o(),ae=l([]),le=1;async function oe(){const e=u.query.id;if(e){const t=await U(`/comment/getList?articleId=${e}&page=${le}&limit=10`);if(200==t.code){const e=t.data.list;te.value=t.data.totalCount,0==e.length&&(O.value=!1),e.forEach(((e,t)=>{ae.push(e)}))}}}async function se(e){G.state.indexSetting.examination?_({showClose:!0,message:"评论成功请耐心等待管理员审核哦！"}):(ae.length=0,le=1,oe().then((()=>{const e=document.getElementById("CommentList");window.scrollTo({top:e.offsetTop,behavior:"smooth"}),_({showClose:!0,message:"评论发布成功😎😎!"})})))}return(e,t)=>(i(),m("div",me,[(i(),m(B,{to:"body"},[r("div",re,[r("div",de,[r("img",{src:p($)(p(F).articlePoster),alt:"nothing",class:"kenburns-top-right"},null,8,["src"])]),r("div",ue,[r("span",pe,h(p(F).title),1),r("span",{class:"author",onClick:t[1]||(t[1]=e=>{return t=p(F).authorName,a=e,f.nickname=t,f.x=a.clientX,void(f.y=a.clientY);var t,a})},h(p(F).authorName),1)]),r("div",ve,[r("span",fe,"时间: "+h(null==p(F).publishTime?0:p(F).publishTime),1),r("span",he,"字数:"+h(null==p(F).wordNum?0:p(F).wordNum),1),r("span",ye,"喜欢数:"+h(null==p(F).likeNum?0:p(F).likeNum),1),r("div",{style:p(g),class:["like",{"bounce-top":p(k),click:p(C)}]},[r("div",{class:"before",style:p(g)},null,4),r("div",{class:"after",style:p(g)},null,4)],6),r("div",{style:p(g),onClick:z,onMouseenter:S,onMouseleave:Y,class:"like"},[r("div",{class:"before",style:p(g)},null,4),r("div",{class:"after",style:p(g)},null,4)],36)])])])),r("div",{class:["article-container",{"to-center":!p(Z),"to-right":p(Z)}]},[r("article",{class:"markdown-body",id:"markdown",innerHTML:p(F).content},null,8,["innerHTML"])],2),r(x,{"enter-active-class":"slide-in-left","leave-active-class":"slide-out-left"},{default:ie((()=>[d(r("div",{class:["table-of-content",p(q)()],id:"toc"},null,2),[[H,p(Z)]])])),_:1}),0==p(F).articleComment?(i(),m("div",ge,[r(W,{id:p(v),class:"comment-component",onCommentCB:se},null,8,["id"]),0!=p(te)?(i(),m("div",ke,[Ce,r("span",null,"("+h(p(te))+")",1)])):(i(),m("div",be,"呜呜呜😭~没有评论,你赶紧评论一个吧！")),r(M,{"enter-active-class":"slide-in-bck-top"},{default:ie((()=>[(i(!0),m(b,null,T(p(ae),(e=>(i(),m(ce,{class:"comment-list",onCommentCB:se,commentItem:e,key:e.id},null,8,["commentItem"])))),128))])),_:1}),r("div",null,[p(Q)?(i(),m(X,{key:0,hasMore:p(O)},null,8,["hasMore"])):I("",!0)])])):(i(),m("div",Te," 博主关闭了该文章的评论功能 ")),r(V,{startX:p(f).x,stratY:p(f).y,nickname:p(f).nickname},null,8,["startX","stratY","nickname"])]))}});Ie.__scopeId="data-v-5972e77d";export default Ie;