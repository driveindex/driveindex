import React, {Context, createContext, useContext, useState} from "react";
import {Loading} from "@hi-ui/loading";

const LoadingContext: Context<{
    isLoading?: boolean
    setLoading?: (loading: boolean) => void
}> = createContext({})


export const LoadingCover = (props: {
    children: React.ReactNode[] | React.ReactNode,
}) => {
    const [ loading, setLoading ] = useState(false)
    return (
        <LoadingContext.Provider value={{isLoading: loading, setLoading: (loading) => setLoading(loading)}}>
            <Loading visible={loading}>
                {props.children}
            </Loading>
        </LoadingContext.Provider>
    )
}

export function useLoading() {
    const { isLoading, setLoading } = useContext(LoadingContext)
    return { isLoading: isLoading!!, setLoading: setLoading!! }
}