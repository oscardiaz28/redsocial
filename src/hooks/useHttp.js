import React, { useCallback, useState } from 'react'

export const useHttp = ({setStateAlert}) => {

    const [data, setData] = useState(null)
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState(null)

    const sendRequest = useCallback( async ({endpoint, method = "GET", multipart = false, body = null}) => {
      setLoading(true)
      setError(null)

      try{
        const config = {
          method,
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify(body)
        }
        const response = await fetch(endpoint, config)
        if(!response.ok){
          const errorData = await response.json()
          throw errorData
        }
        const result = await response.json()
        setData(result)
      }catch(err){
        setStateAlert(true)
        setError(err)
      }finally{
        setLoading(false)
      }

    }, [])

  return {
    data, loading, error, sendRequest
  }

}
