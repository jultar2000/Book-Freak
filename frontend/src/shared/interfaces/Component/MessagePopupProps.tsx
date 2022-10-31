import { Dispatch, SetStateAction } from "react"
import { ResponseStatus } from "../../enums/ResponseStatus"


export interface MessagePopupProps {
    trigger: boolean,
    setTrigger: Dispatch<SetStateAction<boolean>>,
    status: ResponseStatus,
    description : string
}