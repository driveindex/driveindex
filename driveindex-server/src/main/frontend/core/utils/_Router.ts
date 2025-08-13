import {useLocation, useNavigate, useParams} from "react-router-dom";
import {NavigateFunction, useSearchParams} from "react-router";
import queryString from 'query-string';
import {useMemo} from "react";

interface UrlArgsFunction<QT extends Record<string, any>, PT extends Record<string, any>> {
    setQuery(val: Partial<QT>): void
    setParams(val: Partial<PT>): void
    query: Readonly<Partial<QT>>
    param: Readonly<Partial<PT>>
    navigate: NavigateFunction
}

interface EmptyQuery {}
interface EmptyParam {}

export function useUrlArgs<QT extends Record<string, any> = EmptyQuery, PT extends Record<string, any> = EmptyParam>(): UrlArgsFunction<QT, PT> {
    const location = useLocation()
    const navigate = useNavigate()
    const query = useMemo(() => {
        return queryString.parse(location.search, {
            parseNumbers: true,
            parseBooleans: true,
        }) as Readonly<Partial<QT>>
    },  [location.search])
    const param = useParams<PT>() as Readonly<Partial<PT>>

    return {
        query: query,
        param: param,
        setQuery(val: Partial<QT>) {
            const newParams = {
                ...val,
                ...Object.fromEntries(
                    Object.entries(val).filter(([_, value]) => value != null)
                )
            }
            navigate({
                pathname: location.pathname,
                search: queryString.stringify(newParams),
            })
        },
        setParams(val: Partial<PT>) {

        },
        navigate: navigate,
    }
}

export function usePathArgs<PT extends Record<string, any>>() {
    return useUrlArgs<EmptyQuery, PT>()
}
