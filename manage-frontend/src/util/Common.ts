export function copyProperties(source: any, dest: any) {
    Object.keys(source).forEach(key => {
        dest[key] = source[key];
    })
}

export function copyPropertiesHasNull(source: any, dest: any) {
    Object.keys(source).forEach(key => {
        if (source[key] != null) {
            dest[key] = source[key];
        } else {
            if (dest[key] instanceof Array) {
                dest[key] = []
            } else if (dest[key] instanceof String) {
                dest[key] = ''
            } else if (dest[key] instanceof Number) {
                dest[key] = 0
            } else if (dest[key] instanceof Boolean) {
                dest[key] = false
            } else if (dest[key] instanceof Object) {
                dest[key] = {}
            }
        }
    })
}

export function MakeObjEmpty(any: any) {
    Object.keys(any).forEach(key => {
        if (any[key] instanceof Array) {
            any[key] = []
        } else if (any[key] instanceof String) {
            any[key] = ''
        } else if (any[key] instanceof Number) {
            any[key] = 0
        } else if (any[key] instanceof Boolean) {
            any[key] = false
        } else if (any[key] instanceof Object) {
            any[key]={}
        }
    })
}