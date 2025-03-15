import React, { useEffect, useState } from 'react'
import { request } from '../libs/api'

export const useAjax = (url, options = {}) => {
    const {method = "GET", body, multipartFile = false} = options

    const [data, setData] = useState(null)
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState(null)

    useEffect(() => {
        const doOperation = async () => {
            setLoading(true)
            await request({
                endpoint: url,
                body,
                method,
                multipartFile,
                onSuccess: (response) => {
                    setData(response)
                },
                onFailure: (err) => {
                    setError(err)
                }
            })
            setLoading(false)
        }
        doOperation();
    }, [url])
    return {data, loading, error}
}
