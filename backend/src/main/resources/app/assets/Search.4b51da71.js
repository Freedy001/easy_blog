import{e,g as s,l as a,i as t,f as l,h as c,j as n,k as r,m as i,n as o,o as d,q as u,a as f}from"./index.c7fc0e38.js";import{d as g,z as h,s as p,r as m,g as v,w as y,f as M,t as T,o as L,c as k,b as H,F as S,n as q,u as $,h as _,q as C,p as j,a as x}from"./vendor.03637da4.js";import{_ as b}from"./LoadMore.d34531f5.js";const w=C();j("data-v-5e7f55cc");const E={class:"search-container"},I={class:"search-item"},D={class:"title-content"},P={class:"content"},z={class:"details-area"},F={class:"details-item"},Y={class:"category-tag"},A={class:"category"},B={class:"tag-container"},G={class:"teg"},J={key:0,class:"empty"},K=H("span",null,"TMPTY",-1);x();var N=g({expose:[],setup(C){g({SearchInput:e,LoadMore:b}),h();const j=p();let x=m(!0),N=m(!1);v((()=>{j.query.searchString&&V(j.query.searchString),document.body.onscroll=({srcElement:e})=>{const s=e.scrollingElement;s.scrollTop+s.clientHeight>s.scrollHeight-50&&x.value&&(N.value=!0,R++,U().then((()=>{N.value=!x.value})))}})),y((()=>j.query.searchString),(e=>{V(j.query.searchString)}));let O,Q=M([]),R=1;async function U(){const e=await s(`/search/doSearch?searchString=${O}&page=${R}`);if(200==e.code){const s=e.data;0==s.length&&(x.value=!1),s.forEach(((e,s)=>{Q.push(e)}))}}async function V(e){Q.length=0,R=1,O=e,U()}return(s,g)=>{const h=T("el-tooltip");return L(),k("div",E,[H(e,{class:"search-input",showLogo:!0}),H("div",{class:["search-result",$(f)()]},[(L(!0),k(S,null,q($(Q),(e=>(L(),k("div",I,[H("div",D,[H("h2",{class:"title",innerHTML:e.title,onClick:a=>s.$router.push(`article?id=${e.id}`)},null,8,["innerHTML","onClick"]),H("div",P,[H("img",{src:$(a)(e.articlePoster),class:"img-box",alt:"asd",onClick:a=>s.$router.push(`article?id=${e.id}`)},null,8,["src","onClick"]),H("div",z,[(L(!0),k(S,null,q(e.hitItem,(e=>(L(),k("div",F,[H(h,{placement:"top",content:"articleDesc"==e.field?"文章描述":"文章内容",effect:$(t)()?"dark":"light"},{default:w((()=>[H("img",{src:"articleDesc"==e.field?$(t)()?$(l):$(c):$(t)()?$(n):$(r),alt:""},null,8,["src"])])),_:2},1032,["content","effect"]),H("p",{innerHTML:e.content},null,8,["innerHTML"])])))),256))])])]),H("div",Y,[H("div",A,[H(h,{placement:"top",content:"分类",effect:$(t)()?"dark":"light"},{default:w((()=>[H("img",{src:$(t)()?$(i):$(o),alt:""},null,8,["src"])])),_:1},8,["effect"]),H("span",{innerHTML:e.articleCategory},null,8,["innerHTML"])]),H("div",B,[(L(!0),k(S,null,q(e.articleTags,(e=>(L(),k("div",G,[H(h,{placement:"top",content:"标签",effect:$(t)()?"dark":"light"},{default:w((()=>[H("img",{src:$(t)()?$(d):$(u),alt:""},null,8,["src"])])),_:1},8,["effect"]),H("span",{innerHTML:e},null,8,["innerHTML"])])))),256))])])])))),256))],2),0==$(Q).length?(L(),k("div",J,[H("img",{src:$("/assets/empty.df445c2e.svg"),alt:""},null,8,["src"]),K])):_("",!0),H("div",null,[$(N)?(L(),k(b,{key:0,hasMore:$(x)},null,8,["hasMore"])):_("",!0)])])}}});N.__scopeId="data-v-5e7f55cc";export default N;
