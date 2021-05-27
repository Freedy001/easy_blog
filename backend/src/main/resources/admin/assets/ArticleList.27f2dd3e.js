import{g as e,p as t}from"./http.c8a04791.js";import{d as a,g as l,f as s,G as r,v as o,r as i,h as n,o as u,c,b as d,s as p,C as f,F as m,K as g,u as b,q as h,p as w,a as y,x as v}from"./vendor.7ce22028.js";import{_ as C}from"./ArticleSettingDrawer.ca183230.js";import"./index.455f64ef.js";import"./ImgDrawer.2905be4e.js";import"./FullScreen.6c2fcf19.js";import"./CategoryOrTagCard.c648ae9b.js";const _=h();w("data-v-2dbf9507");const k={class:"root"},x={style:{width:"100%",height:"auto","word-break":"break-all",overflow:"hidden"}},T={class:"status"},S={class:"test"},$=p("编辑"),j=p("设置 "),D=p("删除");y();var O=a({expose:[],setup(h){a({ArticleSettingDrawer:C});const{proxy:w}=v(),y=l(),O=s();let I=r([]);o((async()=>{z().then()}));let N=1,q=i(1);async function z(){let t;if(t=await e(`/article/list?page=${N}&limit=16`),200==t.code){I.length=0,q.value=t.data.totalPage;t.data.list.forEach(((e,t)=>{switch(e.articleStatus){case 1:e.dotColor="#fdf000",e.status="未发布";break;case 2:e.dotColor="#f6074e",e.status="回收站";break;case 3:e.dotColor="#52c41a",e.status="已发布";break;case 4:e.dotColor="#10fcf5",e.status="顶置";break;case 5:e.dotColor="#125ee3",e.status="推荐"}I.push(e)}))}else w.$notify.error({title:"出差啦😢！",message:t.msg})}function E(e){N=e,z()}let A=i(),B=i(!1),F=i();async function P(e){let a=[],l=[];e.tagValue.forEach(((e,t)=>{"number"==typeof e?a.push(e):"string"==typeof e&&l.push(e)}));const s=await t("/article/saveOrUpdate",{id:A.value,title:e.title,publishTime:e.publishTime.getTime(),isComment:e.isComment,articleStatus:e.articleStatus,isOverhead:e.isOverhead,authorId:e.authorId,articleCategoryId:e.category,articleDesc:e.desc,articlePoster:e.url,existedTags:a,notExistedTag:l});200==s.code?(w.$notify({title:"成功",message:"保存成功!",type:"success"}),I.length=0,N=1,z().then()):w.$notify.error({title:"出差啦😢！",message:s.msg})}return(t,a)=>{const l=n("el-tag"),s=n("el-form-item"),r=n("el-form"),o=n("el-table-column"),i=n("el-button"),h=n("el-table"),v=n("el-pagination");return u(),c("div",k,[d(h,{class:"article-table",data:b(I),style:{width:"100%"}},{default:_((()=>[d(o,{type:"expand"},{default:_((e=>[d(r,{"label-position":"left",inline:"",class:"demo-table-expand"},{default:_((()=>[d(s,{label:"分类"},{default:_((()=>[d(l,null,{default:_((()=>[p(f(e.row.articleCategory),1)])),_:2},1024)])),_:2},1024),d(s,{label:"标签"},{default:_((()=>[(u(!0),c(m,null,g(e.row.articleTags,(e=>(u(),c(l,{type:"success"},{default:_((()=>[p(f(e),1)])),_:2},1024)))),256))])),_:2},1024),d(s,{label:"作者"},{default:_((()=>[d("span",null,f(e.row.authorName),1)])),_:2},1024),d(s,{label:"发布时间"},{default:_((()=>[d("span",null,f(e.row.publishTime),1)])),_:2},1024),d(s,{label:"访问数"},{default:_((()=>[d("span",null,f(e.row.visitNum),1)])),_:2},1024),d(s,{label:"更新时间"},{default:_((()=>[d("span",null,f(e.row.updateTime),1)])),_:2},1024),d(s,{label:"文章描述"},{default:_((()=>[d("div",x,f(e.row.articleDesc),1)])),_:2},1024)])),_:2},1024)])),_:1}),d(o,{label:"标题","min-width":"200px",prop:"title"}),d(o,{label:"状态"},{default:_((e=>[d("div",T,[d("span",{class:"dot",style:{backgroundColor:e.row.dotColor}},null,4),d("span",S,f(e.row.status),1)])])),_:1}),d(o,{label:"点赞数"},{default:_((e=>[d("span",null,f(e.row.likeNum),1)])),_:1}),d(o,{label:"评论数"},{default:_((e=>[d("span",null,f(e.row.commentNum),1)])),_:1}),d(o,{label:"操作","min-width":"250px"},{default:_((t=>[d(i,{type:"primary",size:"mini",onClick:e=>{return a=t.row.id,void(""!==O.state.articleContent?w.$confirm("你貌似还有文章没保存，直接编辑文章将会覆盖你原来的文章,你确定要继续吗?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((()=>{y.push({path:"/index/article",query:{id:a}})})):y.push({path:"/index/article",query:{id:a}}));var a},round:""},{default:_((()=>[$])),_:2},1032,["onClick"]),d(i,{type:"success",size:"mini",onClick:e=>{return a=t.row.id,l=t.row.articleStatus,F.value=l,A.value=a,void(B.value=!B.value);var a,l},round:""},{default:_((()=>[j])),_:2},1032,["onClick"]),d(i,{type:"danger",size:"mini",onClick:a=>async function(t){const a=await e(`/article/delete?ids=${t}`);200==a.code?(w.$notify({title:"成功",message:"删除成功!",type:"success"}),I.length=0,N=1,z().then()):w.$notify.error({title:"出差啦😢！",message:a.msg})}(t.row.id),round:""},{default:_((()=>[D])),_:2},1032,["onClick"])])),_:1})])),_:1},8,["data"]),d(v,{small:"",background:"",layout:"prev, pager, next","page-count":b(q),onCurrentChange:E},null,8,["page-count"]),d(C,{id:b(A),status:b(F),isOpenDrawer:b(B),onSaveCallback:P},null,8,["id","status","isOpenDrawer"])])}}});O.__scopeId="data-v-2dbf9507";export default O;
