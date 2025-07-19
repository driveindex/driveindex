import {useLocation, useNavigate} from "react-router-dom";
import {NavigateFunction} from "react-router";
import queryString from 'query-string';
import {useMemo} from "react";

interface QueryLocationFunction<T extends Record<string, any>> {
    set(val: T): void;
    navigate: NavigateFunction;
}

export function useQueryLocation<T extends Record<string, any>>(): QueryLocationFunction<T> & T {
    const location = useLocation()
    const navigate = useNavigate()
    const params = useMemo(() => {
        return queryString.parse(location.search, {
            parseNumbers: true,
            parseBooleans: true,
        }) as T
    },  [location.search])

    return {
        ...params,
        set: (params: T) => {
            const newParams = {
                ...params,
                ...Object.fromEntries(
                    Object.entries(params).filter(([_, value]) => value != null)
                )
            }
            navigate({
                pathname: location.pathname,
                search: queryString.stringify(newParams),
            })
        },
        navigate: navigate,
    }
}
