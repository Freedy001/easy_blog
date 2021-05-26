import axios from 'axios'
import {ElMessage} from "element-plus";
import router from "./router";
const baseURL = import.meta.env.DEV ? "http://192.168.1.105:1000/backend" : ""
const ResourceURL = import.meta.env.DEV ? "http://192.168.1.105:1000" : ""

export interface ILogin {
    username: string,
    password: string
}

export async function login(info: ILogin) {
    console.log(info)
    let {data} = await axios.post(`${baseURL}/login?username=${info.username}&password=${info.password}`);
    if (data.code === 201) {
        localStorage.setItem("Authorization", data.token);
        return true;
    } else {
        return false;
    }
}

export async function logout() {
    const authorization = localStorage.getItem("Authorization");
    const {data} = await axios.get(`${baseURL}/logout`, {
        headers: {'Authorization': authorization}
    });
    if (data.code == 200) {
        localStorage.removeItem("Authorization")
        ElMessage({
            showClose: true,
            message: data.msg,
        });
    } else {
        ElMessage({
            showClose: true,
            message: data.msg,
            type: 'error'
        });
    }
}

export async function get(uri: string) {
    const authorization = localStorage.getItem("Authorization");
    const {data} = await axios.get(baseURL + uri, {
        headers: {'Authorization': authorization}
    });
    if (data.code == 2001) {
        localStorage.removeItem("Authorization")
        await router.push('/login')
    }
    return data
}

export async function post(uri: string, dataFiled: any) {
    const authorization = localStorage.getItem("Authorization");
    const {data} = await axios.post(baseURL + uri, dataFiled, {
        headers: {'Authorization': authorization}
    });
    if (data.code == 2001) {
        localStorage.removeItem("Authorization")
        await router.push('/login')
    }
    return data
}

export function loadResource(uri: string) {
    if ((""+uri).startsWith("http")) {
        return uri;
    } else {
        return ResourceURL + uri;
    }
}

export async function getFrontApi(uri: string) {
    const {data} = await axios.get(ResourceURL + uri);
    return data;
}
