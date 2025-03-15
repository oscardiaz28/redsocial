import React, { Children, createContext, use, useEffect, useState } from 'react'
import { request } from '../libs/api'

export const PostContext = createContext()

export const PostProvider = ({children}) => {

    const [posts, setPosts] = useState([])
    const [endpoint, setEndpoint] = useState("")
    const [loading, setLoading] = useState(false)

    const getPosts = async () => {
        setLoading(true)
        await request({
            endpoint,
            onSuccess: (response) => {
                console.log(response)
                setPosts(response?.posts)
            },
            onFailure: (err) => {
                console.log(err)
            }
        })
        setLoading(false)
    }
    useEffect(() => {
        if(endpoint !== ""){
            setPosts([])
            getPosts()
        }
    }, [endpoint])

    return (
        <PostContext.Provider value={{posts, setEndpoint, loading}}>
            {children}
        </PostContext.Provider>
    )

}
