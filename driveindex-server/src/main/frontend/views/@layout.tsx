import {useViewConfig} from '@vaadin/hilla-file-router/runtime.js';
import {useEffect} from 'react';
import {Outlet} from 'react-router';
import {Signal, signal, effect} from '@vaadin/hilla-react-signals';

const vaadin = window.Vaadin as {
    documentTitleSignal: Signal<string>;
};
vaadin.documentTitleSignal = signal('');
effect(() => {
    document.title = vaadin.documentTitleSignal.value;
});

const MainLayout = () => {
    const currentTitle = useViewConfig()?.title ?? '';

    useEffect(() => {
        vaadin.documentTitleSignal.value = currentTitle;
    });

    return (
        <Outlet />
    );
}

export default MainLayout;
