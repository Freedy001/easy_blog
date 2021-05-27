import{g as e,p as a}from"./http.c8a04791.js";import{d as l,r as t,G as r,v as o,D as n,h as s,o as i,c,b as d,C as u,u as m,s as p,e as g,F as y,K as f,q as h,p as b,a as v,x as $}from"./vendor.7ce22028.js";const I=h();b("data-v-39e7318c");const _={class:"card-row"},k=d("br",null,null,-1),x={class:"button-area"},V=p("返回"),w=p("删除"),C=d("br",null,null,-1);v();var U=l({expose:[],props:["name"],emits:["callback"],setup(l){const{proxy:h}=$();let b=t(!1),v=r([]),U=r({id:"",name:"",url:"",priority:"",creatorId:""}),N=r([]);function j(){for(let e=0;e<v.length;e++)v[e]=!1;b.value=!1,E()}async function q(){let e;if(e="标签"==h.name?{id:U.id,tagName:U.name,tagImgUrl:U.url,priority:U.priority,creatorId:U.creatorId}:{id:U.id,categoryName:U.name,categoryImgUrl:U.url,priority:U.priority,creatorId:U.creatorId},b.value){const l=await a(`/${"分类"==h.name?"category":"tag"}/update`,e);200==l.code?(h.$emit("callback"),F().then(),h.$notify({title:"成功",message:"更新成功!",type:"success"})):h.$notify.error({title:"错误",message:`更新失败！\n${l.msg}`})}else{e.id=null;const l=await a(`/${"分类"==h.name?"category":"tag"}/save?`,e);200==l.code?(h.$emit("callback"),F().then(),E(),h.$notify({title:"成功",message:"保存成功!",type:"success"})):h.$notify.error({title:"错误",message:`保存失败！\n${l.msg}`})}}async function D(){const a=await e(`/${"分类"==h.name?"category":"tag"}/delete?ids=${U.id}`);200==a.code?(F().then(),h.$notify({title:"成功！",message:"删除成功!",type:"success"})):h.$notify.error({title:"删除失败！",message:`${a.msg}`}),E()}function E(){U.id="",U.name="",U.url="",U.priority=""}async function F(){const a=await e(`/${"分类"==h.name?"category":"tag"}/list?page=1&limit=50&sidx=priority&order=asc`);if(200==a.code){N.length=0;let e=a.page.list;e.forEach(((e,a)=>{N.push({id:e.id,name:"分类"==h.name?e.categoryName:e.tagName,url:"分类"==h.name?e.categoryImgUrl:e.tagImgUrl,priority:e.priority,creatorId:e.creatorId})}));for(let a=0;a<e.length;a++)v[a]=!1}else n({showClose:!0,message:`网络异常！\n ${a.msg}`,type:"error"})}return o((()=>{F().then()})),(e,a)=>{const t=s("el-input"),r=s("el-form-item"),o=s("el-option"),n=s("el-select"),h=s("el-form"),$=s("el-button"),E=s("el-card"),F=s("el-check-tag");return i(),c("div",_,[d(E,{class:"box-card form-area"},{default:I((()=>[d("div",null,[d("h1",null,u(l.name),1),k,d(h,{model:m(U)},{default:I((()=>[d(r,{label:"名称"},{default:I((()=>[d(t,{type:"text",modelValue:m(U).name,"onUpdate:modelValue":a[1]||(a[1]=e=>m(U).name=e),placeholder:"请输入标签名称","show-word-limit":""},null,8,["modelValue"])])),_:1}),d(r,{label:"图片url"},{default:I((()=>[d(t,{type:"text",modelValue:m(U).url,"onUpdate:modelValue":a[2]||(a[2]=e=>m(U).url=e),placeholder:"请输入图片url"},null,8,["modelValue"])])),_:1}),d(r,{label:"优先级"},{default:I((()=>[d(n,{modelValue:m(U).priority,"onUpdate:modelValue":a[3]||(a[3]=e=>m(U).priority=e),placeholder:"请选择标签优先级"},{default:I((()=>[d(o,{label:"最高",value:1}),d(o,{label:"高",value:2}),d(o,{label:"普通",value:3}),d(o,{label:"低",value:4}),d(o,{label:"最低",value:5})])),_:1},8,["modelValue"])])),_:1})])),_:1},8,["model"])]),d("div",x,[d("div",null,[d($,{type:"primary",round:"",onClick:q},{default:I((()=>[p(u(m(b)?"修改":"添加"),1)])),_:1}),m(b)?(i(),c($,{key:0,round:"",onClick:j},{default:I((()=>[V])),_:1})):g("",!0)]),m(b)?(i(),c($,{key:0,type:"danger",round:"",onClick:D},{default:I((()=>[w])),_:1})):g("",!0)])])),_:1}),d(E,{class:"box-card tag-area"},{default:I((()=>[d("h1",null,"所有"+u(l.name),1),C,(i(!0),c(y,null,f(m(N),((e,a)=>(i(),c(F,{key:a,onChange:e=>function(e){for(let a=0;a<v.length;a++)v[a]=!1;v[e]=!0,b.value=!0,U.id=N[e].id,U.name=N[e].name,U.url=N[e].url,U.priority=N[e].priority,U.creatorId=N[e].creatorId}(a),checked:m(v)[a]},{default:I((()=>[p(u(e.name),1)])),_:2},1032,["onChange","checked"])))),128))])),_:1})])}}});U.__scopeId="data-v-39e7318c";export{U as _};
