import {useViewConfig} from '@vaadin/hilla-file-router/runtime.js';
import {Suspense, useEffect} from 'react';
import {Outlet} from 'react-router';
import {Signal, signal, effect} from '@vaadin/hilla-react-signals';
import {ViewportProvider} from "Frontend/core/hooks/useViewport";
import {Loading} from "@hi-ui/hiui";
import {LoadingCover} from "Frontend/core/hooks/useLoading";
import i18next from "i18next";
import I18nextBrowserLanguageDetector from "i18next-browser-languagedetector";
import {initReactI18next} from "react-i18next";
import {resources} from "Frontend/i18next/resources";

i18next.use(I18nextBrowserLanguageDetector)
    .use(initReactI18next)
    .init({
        resources,
        fallbackLng: "zh",
        lng: navigator.language,
    })

const vaadin = window.Vaadin as {
    documentTitleSignal: Signal<string>;
};
vaadin.documentTitleSignal = signal('');
effect(() => {
    document.title = vaadin.documentTitleSignal.value;
});

export default function MainLayout() {
    const currentTitle = useViewConfig()?.title ?? '';

    useEffect(() => {
        vaadin.documentTitleSignal.value = currentTitle;
    });

    return (
        <ViewportProvider>
            <Suspense fallback={<Loading full={true}/>}>
                <LoadingCover>
                    <Outlet/>
                </LoadingCover>
            </Suspense>
        </ViewportProvider>
    );
}