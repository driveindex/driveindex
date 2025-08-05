import CryptoJS from 'crypto-js';

export function asInitials(full: string | undefined): string {
    if (full === undefined) {
        return ""
    }
    let result: string = full.charAt(0).toUpperCase()
    const list = full.split(" ")
    if (list.length > 1) {
        result += list[1].charAt(0).toUpperCase()
    }
    return result
}

export function asSha256(str: string): string {
    return CryptoJS.SHA256(str).toString(CryptoJS.enc.Hex)
}
