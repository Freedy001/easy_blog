import{d as e,r as t,c as o,a as n,t as s,u as r,F as l,p as a,b as u,e as c,f as d,g as i,o as m,w as p,h as f,i as _,j as h,k as v,l as b,m as g}from"./vendor.b9c12130.js";!function(e=".",t="__import__"){try{self[t]=new Function("u","return import(u)")}catch(o){const n=new URL(e,location),s=e=>{URL.revokeObjectURL(e.src),e.remove()};self[t]=e=>new Promise(((o,r)=>{const l=new URL(e,n);if(self[t].moduleMap[l])return o(self[t].moduleMap[l]);const a=new Blob([`import * as m from '${l}';`,`${t}.moduleMap['${l}']=m;`],{type:"text/javascript"}),u=Object.assign(document.createElement("script"),{type:"module",src:URL.createObjectURL(a),onerror(){r(new Error(`Failed to import: ${e}`)),s(u)},onload(){o(self[t].moduleMap[l]),s(u)}});document.head.appendChild(u)})),self[t].moduleMap={}}}("/assets/"),a("data-v-33b7ed2f");const L=n("h2",null,"哈哈哈哈",-1);u();var E=e({expose:[],setup(e){let a=t("23232");const u=c();console.log(u);const p=d();console.log(p);const f=i();return console.log(f),(e,t)=>(m(),o(l,null,[n("h1",null,s(r(a)),1),L,n("h1",null,s(r(f).state.counter),1),n("button",{onClick:t[1]||(t[1]=e=>r(f).commit("add"))},"加1")],64))}});E.__scopeId="data-v-33b7ed2f";var w=Object.freeze({__proto__:null,[Symbol.toStringTag]:"Module",default:E}),j=e({name:"App",components:{HelloWorld:E}});const y=n("img",{alt:"Vue logo",src:"/assets/logo.03d6d6da.png"},null,-1),R=_("主页"),k=n("span",null,"----",-1),O=_("菜单");let M;j.render=function(e,t,s,r,a,u){const c=f("router-view"),d=f("router-link");return m(),o(l,null,[y,n(c),n("div",null,[n(d,{to:"/"},{default:p((()=>[R])),_:1}),k,n(d,{to:"/hello"},{default:p((()=>[O])),_:1})])],64)};const P={},U=function(e,t){if(!t)return e();if(void 0===M){const e=document.createElement("link").relList;M=e&&e.supports&&e.supports("modulepreload")?"modulepreload":"preload"}return Promise.all(t.map((e=>{if(e in P)return;P[e]=!0;const t=e.endsWith(".css"),o=t?'[rel="stylesheet"]':"";if(document.querySelector(`link[href="${e}"]${o}`))return;const n=document.createElement("link");return n.rel=t?"stylesheet":M,t||(n.as="script",n.crossOrigin=""),n.href=e,document.head.appendChild(n),t?new Promise(((e,t)=>{n.addEventListener("load",e),n.addEventListener("error",t)})):void 0}))).then((()=>e()))},$=[{path:"/",component:()=>U((()=>Promise.resolve().then((function(){return w}))),void 0)},{path:"/hello",component:()=>U((()=>__import__("./test.a9c88f5b.js")),["/assets/test.a9c88f5b.js","/assets/vendor.b9c12130.js"])}];var A=h({history:v(),routes:$}),C=b({state:{counter:0},mutations:{add(e){e.counter++}}});g(j).use(A).use(C).mount("#app");
