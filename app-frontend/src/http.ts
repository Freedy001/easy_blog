import axios from 'axios'
const baseURL=import.meta.env.DEV?"http://localhost:1000/backend":""
const ResourceURL=import.meta.env.DEV?"http://localhost:1000":""

export async function get(uri:string) {
    const {data} =await axios.get(baseURL+uri);
    return data
}

export async function post(uri:string,dataFiled:any) {
    const {data} =await axios.post(baseURL+uri,dataFiled);
    return data
}

export function loadResource(uri:string){
    return ResourceURL+uri;
}