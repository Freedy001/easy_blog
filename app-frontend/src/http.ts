import axios from 'axios'

const baseURL = import.meta.env.DEV ? "http://192.168.1.105:1000/frontend" : "/frontend"
const ResourceURL = import.meta.env.DEV ? "http://192.168.1.105:1000" : ""

export async function get(uri: string) {
    const {data} = await axios.get(baseURL + uri);
    return data
}

export async function post(uri: string, dataFiled: any) {
    const {data} = await axios.post(baseURL + uri, dataFiled);
    return data
}

export function loadResource(uri: any) {
    if (("" + uri).startsWith("http")) {
        return uri;
    } else {
        return ResourceURL + uri;
    }
}

export function loadHdResource(uri: any) {
    console.log(uri)
    return loadResource(converter(uri))
}

function converter(url: string) {
    if (("" + url).match(/\?x-oss-process/)) {
        return url.split("\?x-oss-process")[0];
    }
    if (("" + url).match(/\/image\//)) {
        let split = url.split("-", 5);
        return split[0] + "-" + split[1] + "-" + split[2] + "-" + split[4];
    }
    return url;
}