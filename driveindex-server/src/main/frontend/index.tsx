import {Suspense, createElement, useState, useEffect} from 'react';
import {createRoot} from 'react-dom/client';
import {RouterProvider} from 'react-router';
import {router} from 'Frontend/generated/routes.js';
import {ViewportProvider} from './core/hooks/useViewport';
import {Loading} from "@hi-ui/loading";
import {LoadingCover} from "Frontend/core/hooks/useLoading";
import {AuthProvider} from "Frontend/core/security/auth"

import {i18n} from '@vaadin/hilla-react-i18n';

const RealApp = () => {
    return (
        <ViewportProvider>
            <Suspense fallback={<Loading full={true}/>}>
                <LoadingCover>
                    <AuthProvider>
                        <RouterProvider router={router}/>
                    </AuthProvider>
                </LoadingCover>
            </Suspense>
        </ViewportProvider>
    )
}

const App = () => {
    const [i18nLoaded, setI18nLoaded] = useState(false);

    useEffect(() => {
        i18n.configure().then(() => {
            setI18nLoaded(true)
        })
    }, [setI18nLoaded])

    return (
        i18nLoaded ? <RealApp /> : <Loading />
    );
}

const outlet = document.getElementById('outlet')!;
let root = (outlet as any)._root ?? createRoot(outlet);
(outlet as any)._root = root;
root.render(createElement(App));

