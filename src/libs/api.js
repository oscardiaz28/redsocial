import { clientAxios } from "../../config/axios"

export const request = async ({
    endpoint,
    method = "GET",
    body,
    multipartFile = false,
    onSuccess,
    onFailure
}) => {
    try{
        const config = {
            method, 
            url: endpoint,
            withCredentials: true
        }
        if(!multipartFile){
            config.headers = {"Content-Type": "application/json"}
        }
        if(method !== "GET" && body){
            config.data = multipartFile ? body : JSON.stringify(body)
        }
       
        const {data} = await clientAxios(config)
        onSuccess(data)
       
    }catch(err){
        onFailure(err?.response?.data || err.message)
    }

}