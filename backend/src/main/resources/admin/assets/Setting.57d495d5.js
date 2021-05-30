import{g as e,a as l,c as t,p as a}from"./http.379cb87b.js";import{_ as o,c as s,n as i}from"./Common.ca5a7a9a.js";import{d as n,t as d,f as c,v as m,r,E as u,H as p,h as f,o as g,c as y,b as h,u as v,F as V,K as b,e as x,I as w,q as S,p as k,s as C,a as U,x as _}from"./vendor.d4f367cf.js";import"./index.d5e32cea.js";import"./FullScreen.589b3e35.js";const $=S();k("data-v-f4c23ab0");const I={class:"indexContainer"},N={class:"common-setting"},T={class:"item"},A=h("h1",null,"博客标题",-1),M={class:"item"},j=h("h1",null,"博客域名",-1),P={class:"item"},H=h("h1",null,"首页文章",-1),L={class:"logo-img item"},D={class:"logo"},E=h("h1",null,"Logo",-1),F={class:"img"},q=h("h1",null,"首页图片",-1),B={class:"item color"},O=h("h1",null,"首页色系",-1),K={class:"show-box"},z={class:"item"},G=h("h1",null,"页脚信息",-1),J=C("保存"),Q={class:"smtp"},R={class:"item"},W=h("h1",null,"SMTP地址",-1),X={class:"item"},Y=h("h1",null,"SSL端口",-1),Z={class:"item"},ee=h("h1",null,"邮箱账号",-1),le={class:"item"},te=h("h1",null,"邮箱密码或授权码",-1),ae={class:"item"},oe=h("h1",null,"发送人",-1),se=C("保存"),ie={class:"comment"},ne={class:"item"},de=h("h1",null,"评论需要审核",-1),ce={class:"item"},me=h("h1",null,"新评论通知",-1),re={class:"item"},ue=h("h1",null,"评论回复通知对方",-1),pe=C("保存 "),fe={class:"other"},ge={class:"item"},ye={class:"about"},he=h("h1",null,"关于",-1),ve=C("去编辑"),Ve={class:"root"};U();var be=n({expose:[],setup(S){const{proxy:k}=_();n({ImgDrawer:o});const C=d(),U=c();let be=/setting-common/,xe=/setting-smtp/,we=/setting-comment/,Se=/setting-about/;m((()=>{be.test(U.state.userInfo.permissionStr)&&Pe(),C.query.toForth&&(Ce.value="4")}));let ke=r(0),Ce=r("1");u(Ce,(l=>{"1"==l?Pe():"2"==l?async function(){const l=await e("/setting/getSMTPSetting");if(200==l.code){const e=l.data;Object.keys(e).forEach(((l,t)=>{Le[l]=e[l]}))}else 3001==l.code?i():k.$notify({title:"出差啦😢！",message:l.msg,type:"error"})}():"3"==l?async function(){const l=await e("/setting/getCommentSetting");if(200==l.code){const e=l.data;Object.keys(e).forEach(((l,t)=>{Ee[l]=e[l]}))}else 3001==l.code?i():k.$notify({title:"出差啦😢！",message:l.msg,type:"error"})}():"4"==l&&async function(){const e=await t("/frontend/article/get?id=1");200==e.code?s(e.data,qe):3001==e.code&&i()}()}));let Ue=p({indexArticle:{id:"",title:""}}),_e=p({borderBottom:"500vh solid rgba(45, 37, 73, 0.7)"});u((()=>Ue.indexColor),(()=>{_e.borderBottom=`500vh solid ${Ue.indexColor}`}));let $e,Ie=r(!1),Ne=p([]);async function Te(e){Ie.value=!0;const l=await t(`/frontend/search/getSuggestions?queryString=${e}`);if(200==l.code){Ne.length=1;l.data.forEach(((e,l)=>{Ne[0].id!=e.id&&Ne.push({id:e.id,label:e.title})})),Ie.value=!1}else 3001===l.code&&i()}function Ae(){ke.value++,$e="logo"}function Me(){ke.value++,$e="poster"}function je(e){Ue[$e]=e}async function Pe(){const l=await e("/setting/getCommonSetting");if(200==l.code){const e=l.data;s(e,Ue),Ne.push({id:e.indexArticle.id,label:e.indexArticle.title})}else 3001==l.code?i():k.$notify({title:"出差啦😢！",message:l.msg,type:"error"})}async function He(){const e=await a("/setting/saveCommon",Ue);200==e.code?k.$notify({title:"成功",message:"保存成功!",type:"success"}):3001==e.code?i():k.$notify({title:"出差啦😢！",message:e.msg,type:"error"})}u((()=>Ue.indexArticle.id),(e=>{Ne.forEach((l=>{l.id==e&&(Ue.indexArticle.title=l.label)}))}));let Le=p({});async function De(){const e=await a("/setting/saveSMTP",Le);200==e.code?k.$notify({title:"成功",message:"保存成功!",type:"success"}):3001==e.code?i():k.$notify({title:"出差啦😢！",message:e.msg,type:"error"})}let Ee=p({});async function Fe(){const e=await a("/setting/saveComment",Ee);200==e.code?k.$notify({title:"成功",message:"保存成功!",type:"success"}):3001==e.code?i():k.$notify({title:"出差啦😢！",message:e.msg,type:"error"})}let qe=p({});return(e,t)=>{const a=f("el-input"),s=f("el-option"),i=f("el-select"),n=f("el-color-picker"),d=f("el-button"),c=f("el-tab-pane"),m=f("el-switch"),r=f("el-tabs");return g(),y("div",I,[h(r,{modelValue:v(Ce),"onUpdate:modelValue":t[15]||(t[15]=e=>w(Ce)?Ce.value=e:Ce=e)},{default:$((()=>[v(be).test(v(U).state.userInfo.permissionStr)?(g(),y(c,{key:0,label:"常规设置",name:"1"},{default:$((()=>[h("div",N,[h("div",T,[A,h(a,{modelValue:v(Ue).blogTitle,"onUpdate:modelValue":t[1]||(t[1]=e=>v(Ue).blogTitle=e),placeholder:"请输入内容"},null,8,["modelValue"])]),h("div",M,[j,h(a,{modelValue:v(Ue).webSiteDomainName,"onUpdate:modelValue":t[2]||(t[2]=e=>v(Ue).webSiteDomainName=e),placeholder:"请输入内容"},null,8,["modelValue"])]),h("div",P,[H,h(i,{modelValue:v(Ue).indexArticle.id,"onUpdate:modelValue":t[3]||(t[3]=e=>v(Ue).indexArticle.id=e),filterable:"",remote:"","reserve-keyword":"",placeholder:"请输入关键词","remote-method":Te,loading:v(Ie)},{default:$((()=>[(g(!0),y(V,null,b(v(Ne),(e=>(g(),y(s,{key:e.id,label:e.label,value:e.id},null,8,["label","value"])))),128))])),_:1},8,["modelValue","loading"])]),h("div",L,[h("div",D,[E,h("img",{src:v(l)(v(Ue).logo),onClick:Ae,alt:""},null,8,["src"])]),h("div",F,[q,h("img",{src:v(l)(v(Ue).poster),onClick:Me,alt:""},null,8,["src"])])]),h("div",B,[O,h(n,{modelValue:v(Ue).indexColor,"onUpdate:modelValue":t[4]||(t[4]=e=>v(Ue).indexColor=e),"show-alpha":"",class:"color-picker"},null,8,["modelValue"]),h("div",K,[h("div",{class:"triangle",style:v(_e)},null,4),h("img",{src:v(l)(v(Ue).poster),alt:""},null,8,["src"])])]),h("div",z,[G,h(a,{type:"textarea",rows:2,placeholder:"可以填入HTML",modelValue:v(Ue).footInfo,"onUpdate:modelValue":t[5]||(t[5]=e=>v(Ue).footInfo=e)},null,8,["modelValue"])]),h(d,{type:"primary",onClick:He},{default:$((()=>[J])),_:1})])])),_:1})):x("",!0),v(xe).test(v(U).state.userInfo.permissionStr)?(g(),y(c,{key:1,label:"SMTP设置",name:"2"},{default:$((()=>[h("div",Q,[h("div",R,[W,h(a,{modelValue:v(Le).emailHostName,"onUpdate:modelValue":t[6]||(t[6]=e=>v(Le).emailHostName=e),placeholder:"请输入SMTP地址"},null,8,["modelValue"])]),h("div",X,[Y,h(a,{modelValue:v(Le).sslPort,"onUpdate:modelValue":t[7]||(t[7]=e=>v(Le).sslPort=e),placeholder:"请输入SSL端口"},null,8,["modelValue"])]),h("div",Z,[ee,h(a,{modelValue:v(Le).emailFrom,"onUpdate:modelValue":t[8]||(t[8]=e=>v(Le).emailFrom=e),placeholder:"请输入邮箱账号"},null,8,["modelValue"])]),h("div",le,[te,h(a,{modelValue:v(Le).emailAuthentication,"onUpdate:modelValue":t[9]||(t[9]=e=>v(Le).emailAuthentication=e),placeholder:"请输入邮箱密码或授权码"},null,8,["modelValue"])]),h("div",ae,[oe,h(a,{modelValue:v(Le).senderName,"onUpdate:modelValue":t[10]||(t[10]=e=>v(Le).senderName=e),placeholder:"请输入发送人"},null,8,["modelValue"])]),h(d,{type:"primary",onClick:De},{default:$((()=>[se])),_:1})])])),_:1})):x("",!0),v(we).test(v(U).state.userInfo.permissionStr)?(g(),y(c,{key:2,label:"评论设置",name:"3"},{default:$((()=>[h("div",ie,[h("div",ne,[de,h(m,{modelValue:v(Ee).examination,"onUpdate:modelValue":t[11]||(t[11]=e=>v(Ee).examination=e)},null,8,["modelValue"])]),h("div",ce,[me,h(m,{modelValue:v(Ee).newCommentNotification,"onUpdate:modelValue":t[12]||(t[12]=e=>v(Ee).newCommentNotification=e)},null,8,["modelValue"])]),h("div",re,[ue,h(m,{modelValue:v(Ee).replayNotification,"onUpdate:modelValue":t[13]||(t[13]=e=>v(Ee).replayNotification=e)},null,8,["modelValue"])]),h(d,{type:"primary",onClick:Fe,style:{"margin-top":"30px"}},{default:$((()=>[pe])),_:1})])])),_:1})):x("",!0),v(Se).test(v(U).state.userInfo.permissionStr)?(g(),y(c,{key:3,label:"关于页面",name:"4"},{default:$((()=>[h("div",fe,[h("div",ge,[h("div",ye,[he,h(d,{type:"primary",onClick:t[14]||(t[14]=l=>e.$router.push("/index/article?id=1")),round:""},{default:$((()=>[ve])),_:1})]),h("div",Ve,[h("article",{class:"markdown-body",id:"markdown",innerHTML:v(qe).content},null,8,["innerHTML"])])])])])),_:1})):x("",!0)])),_:1},8,["modelValue"]),h(o,{isDrawer:v(ke),onClickCallback:je},null,8,["isDrawer"])])}}});be.__scopeId="data-v-f4c23ab0";export default be;
