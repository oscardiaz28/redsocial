import React, { createContext, useEffect, useState } from 'react'
import { request } from '../libs/api'
import axios from 'axios'
import { clientAxios } from '../../config/axios'

const AuthContext = createContext()

export const AuthProvider = ({children}) => {

    const [user, setUser] = useState(null)
    const [loading, setLoading] = useState({
        fetch: true,
        mutation: false
    })
    const [counters, setCounters] = useState(null)

    const getCounters = async (id = -1) => {
        try{
            let url = ""
            url = (id == -1) ? `/users/counters` 
                             : `/users/counters/${id}`
            const responseCounter = await clientAxios.get(url, {
                withCredentials: true
            })
            setCounters(responseCounter.data)
        }catch(err){
            console.log(err)
        }
    }

    const checkAuth = async () => {
        console.log("Se ejecuto checkAuth")
        /*
        await request({
            endpoint: "/users/profile",
            onSuccess: (resp) => {
                getCounters()
                setUser(resp.data)
            }, 
            onFailure: () => {
                setUser(null)
            }
        })*/
        try{
            const response = await clientAxios.get("/users/profile", {
                withCredentials: true
            })
            getCounters()
            setUser(response.data)
        }catch(err){
            setUser(null)
        }
        setLoading({
            ...loading,
            ["fetch"]: false
        })
    }

    const logout = async () => {
        await clientAxios.post("/auth/logout", {}, {
            withCredentials: true
        })
        setCounters(null)
        setUser(null)
    }

    const updateProfile = async (data) => {
        setLoading({
            ...loading,
            ["mutation"]: true
        })
        try{
            const response = await clientAxios.put("/users", data, {
                withCredentials: true
            })
            setUser(response.data?.user)
        }catch(err){
            checkAuth()
            throw err
        }finally{
            setLoading({
                ...loading,
                ["mutation"]: false
            })
        }
    }

    useEffect(() => {
        checkAuth()
    }, [])

  return (
    <AuthContext.Provider value={{
        user,
        counters,
        isAuthenticated: user !== null ? true : false,
        loading,
        checkAuth,
        updateProfile,
        logout,
        getCounters
    }}>
        {children}
    </AuthContext.Provider>
  )

}

export default AuthContext