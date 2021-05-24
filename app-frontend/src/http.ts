import axios from 'axios'
const baseURL=import.meta.env.DEV?"http://192.168.1.105:1000/frontend":""
const ResourceURL=import.meta.env.DEV?"http://192.168.1.105:1000":""

export async function get(uri:string) {
    const {data} =await axios.get(baseURL+uri);
    return data
}

export async function post(uri:string,dataFiled:any) {
    const {data} =await axios.post(baseURL+uri,dataFiled);
    return data
}

export function loadResource(uri:string){
    if ((""+uri).startsWith("http")) {
        return uri;
    } else {
        return ResourceURL + uri;
    }
}