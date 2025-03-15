import React, { useEffect, useState } from 'react'
import { request } from '../../libs/api.js'
import { UserList } from '../../components/common/UserList.jsx'

export const PeoplePage = () => {
  
  const [usuarios, setUsuarios] = useState(null)
  const [loading, setLoading] = useState(false)
  const [following, setFollowing] = useState([])
  const [followers, setFollowers] = useState([])

  useEffect(() => {
    const fetchUsers = async () => {
      setLoading(true)
      await request({
        endpoint: "/users/list",
        onSuccess: (response) => {
          setUsuarios(response.users)
          setFollowing(response.user_following)
          setFollowers(response.user_following_me)
        },
        onFailure: (err) => {
          console.log(err)
        }
      })
      setLoading(false)
    }
    fetchUsers()
  }, [])

  return (
    <div>

      <h3 className='font-bold text-2xl mb-8'>Usuarios</h3>
      <div className='flex flex-col gap-6'>
        {
          loading ? <p>Cargando...</p>
          : <UserList 
              usuarios={usuarios} 
              following={following} 
              setFollowing={setFollowing} 
              followers={followers}
            />
        }
      </div>

    </div>
  )
}
