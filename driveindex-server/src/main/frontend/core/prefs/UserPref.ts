export class UserPref {
    static get Login(): boolean {
        return UserPref.getBoolOrDef("Login", false)
    }
    static set Login(value: boolean) {
        localStorage["Login"] = value
    }

    static get Username(): string {
        return UserPref.getStrOrDef("Username", "")
    }
    static set Username(value: string) {
        localStorage["Username"] = value
    }

    static get Role(): "ADMIN" | "USER" | undefined {
        const role = UserPref.getStrOrDef("Nick", "")
        if (role === "ADMIN" || role === "USER") {
            return role as "ADMIN" | "USER" | undefined
        } else {
            return undefined
        }
    }
    static set Role(value: string)  {
        localStorage["Role"] = value
    }

    static get Nickname(): string {
        return UserPref.getStrOrDef("Nickname", "")
    }
    static set Nickname(value: string)  {
        localStorage["Nickname"] = value
    }

    private static getStrOrDef(key: string, def: string): string {
        return localStorage[key] ?? def
    }
    private static getBoolOrDef(key: string, def: boolean): boolean {
        const value = localStorage[key]
        if (value == null) {
            return def
        } else {
            return value === "true"
        }
    }
}