import ClientType from "Frontend/generated/io/github/driveindex/core/client/ClientType";
import {FormRuleModel} from "@hi-ui/form";

abstract class ClientCreation {
    abstract readonly type: ClientType
    abstract readonly formData: Record<string, FormRuleModel[]>
}

const useClientCreation = (type: ClientType) => {

}
