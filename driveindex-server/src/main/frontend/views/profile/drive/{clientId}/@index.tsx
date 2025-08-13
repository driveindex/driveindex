import {usePathArgs} from "Frontend/core/utils/_Router";

interface ClientListPathArgs {
    clientId: string;
}

const ClientList = () => {
    const urlArgs = usePathArgs<ClientListPathArgs>()
    return (
        <>
        </>
    )
}

export default ClientList
