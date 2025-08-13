import {loadEnv, UserConfigFn} from 'vite';
import { overrideVaadinConfig } from './vite.generated';

const customConfig: UserConfigFn = ({ mode }) => {
    // Here you can add custom Vite parameters
    // https://vitejs.dev/config/
    const env = loadEnv(mode, process.cwd(), '')

    return {
        define: {
            "DRIVEINDEX_DEBUG": env.DRIVEINDEX_DEBUG ?? false
        }
    }
};

export default overrideVaadinConfig(customConfig);
