import React, { useEffect, useState } from 'react'
import { UserList } from '../../../components/common/UserList'
import { request } from '../../../libs/api'

export const FollowPage = () => {

    const [usuarios, setUsuarios] = useState(null)
    const [loading, setLoading] = useState(false)
    const [following, setFollowing] = useState([])
    const [followers, setFollowers] = useState([])

    const fetchFollowing = async () => {
        setLoading(true)
        await request({
            endpoint: `/follows/following`,
            onSuccess: (response) => {
                setUsuarios(response.follows)
                setFollowing(response.user_following)
                setFollowers(response.user_following_me)
            },
            onFailure: (err) => {
                console.log(err)
            }
        })
        setLoading(false)
    }

    useEffect(() => {
        fetchFollowing()
    }, [])

  return (
    <div>

      <h3 className='font-bold text-2xl mb-8'>Siguiendo</h3>
      <div className='flex flex-col gap-6'>
        {
          loading ? <p>Cargando...</p>
          : <UserList 
              usuarios={usuarios} 
              following={following} 
              setFollowing={setFollowing} 
              followers={followers}
              fetchFollowing={fetchFollowing}
            />
        }
      </div>

    </div>
  )
}
