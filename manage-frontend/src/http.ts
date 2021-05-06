import axios from 'axios'
import {ElMessage} from "element-plus";
const baseURL=import.meta.env.DEV?"http://localhost:1000/backend":""
export interface ILogin {
    username:string,
    password:string
}

export async function login(info:ILogin){
    console.log(info)
    let {data} = await axios.post(`${baseURL}/login?username=${info.username}&password=${info.password}`);
    if (data.code===201){
        localStorage.setItem("Authorization", data.token);
        return true;
    }else {
        return false;
    }
}

export async function logout(){
    const authorization = localStorage.getItem("Authorization");
    const {data} =await axios.get(`${baseURL}/logout`,{
        headers: {'Authorization': authorization}
    });
    if (data.code==200){
        localStorage.removeItem("Authorization")
        ElMessage({
            showClose: true,
            message: data.msg,
        });
    }else {
        ElMessage({
            showClose: true,
            message: data.msg,
            type: 'error'
        });
    }
}


export async function get(uri:string) {
    const authorization = localStorage.getItem("Authorization");
    const {data} =await axios.get(baseURL+uri,{
        headers: {'Authorization': authorization}
    });
    if (data.code==2001){
        localStorage.removeItem("Authorization")
        location.href="/"
    }
    return data
}

export async function post(uri:string,dataFiled:any) {
    const authorization = localStorage.getItem("Authorization");
    const {data} =await axios.post(baseURL+uri,dataFiled,{
        headers: {'Authorization': authorization}
    });
    if (data.code==2001){
        localStorage.removeItem("Authorization")
        location.href="/"
    }
    return data
}

