import React, { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { request } from '../../libs/api';
import { FaLink } from 'react-icons/fa';
import { LuMapPin } from 'react-icons/lu';
import toast from 'react-hot-toast'
import useAuth from '../../hooks/useAuth';
import { PostList } from '../../components/common/PostList';
import ProfileSkeleton from '../../components/common/ProfileSkeleton';

const backendUrl = import.meta.env.VITE_BACKEND_URL


export const ProfileUser = () => {
    const {getCounters} = useAuth()
    const { username } = useParams();
    const [profile, setProfile] = useState(null)
    const [counters, setCounters] = useState(null)
    const [loading, setLoading] = useState(true)
    const [posts, setPosts] = useState([])
    const [loadingPots, setLoadingPosts] = useState(true)

    useEffect(() => {
        const fetchData = async () => {
            try {
                let profileData;
                let countersData;
                const [profileResponse, countersResponse] = await Promise.all([
                    request({
                        endpoint: `/users/profile/${username}`,
                        onSuccess: (response) => {
                            profileData = response
                        },
                        onFailure: (err) => {
                            return null
                        }
                    }),
                    request({
                        endpoint: `/users/counters/${username}`,
                        onSuccess: (response) => {
                            countersData = response
                        },
                        onFailure: (err) => {
                            return null
                        }
                    }),
                ])
                setProfile(profileData)
                setCounters(countersData)
                await request({
                    endpoint: `/publications/user/${profileData?.user?._id}`,
                    onSuccess: (resp) => {
                        setPosts(resp.posts)
                    },
                    onFailure: (err) => console.log(err)
                })
                setTimeout(() => {
                    setLoadingPosts(false)
                }, 1000);
            } catch (err) {
                console.log(err)
                console.log("Error fetching data")
            } finally {
                setLoading(false)
            }
        }
        fetchData()
    }, [username])

    const handleUnfollow = async (id) => {
        await request({
            endpoint: `/follows/${id}`,
            method: "DELETE",
            onSuccess: (resp) => {
                setProfile( prev => {
                    return {
                        ...prev,
                        amIFollowing: !profile?.amIFollowing
                    }
                })
                getCounters()
            },
            onFailure: (err) => console.log(err)
        })
    }
    const handleFollow = async (id) => {
        await request({
            endpoint: `/follows/${id}`,
            method: "POST",
            onSuccess: (resp) => {
                setProfile( prev => {
                    return {
                        ...prev,
                        amIFollowing: !profile?.amIFollowing
                    }
                })
                getCounters()
            },
            onFailure: (err) => console.log(err)
        })
    }

    return (
        <div>
            {loading
                ?
                    <ProfileSkeleton />
                : (
                    <div className='hidden lg:block p-4 px-6 border-1 border-gray-800 rounded-md'>
                        <div className='flex flex-col items-start justify-center border-b border-gray-600 pb-6 relative'>
                            {
                                profile?.user?.image.startsWith("avatar-default") ?
                                    <img src={`/avatar-default.jpg`} alt=""
                                        className='rounded-full w-30 h-30 mb-2 object-cover border' />
                                    :
                                    <img src={`${backendUrl}/api/users/avatar/${profile?.user?.image}`} alt=""
                                        className='rounded-full w-30 h-30 mb-2 object-cover' />
                            }
                            <p className='font-bold'>{profile?.user?.name} {profile?.user?.surname} </p>
                            <p className='text-gray-400 text-sm mb-2'>{profile?.user?.bio}</p>
                            <p className='pt-2 font-semibold text-gray-300'>{profile?.user?.position}</p>
                            {
                                profile?.amIFollowing ? (
                                    <button className='btn btn-sm bg-[#6665EC] text-white absolute top-0 -right-2 rounded-md'
                                    onClick={ () => handleUnfollow(profile?.user?._id) }
                                    >
                                        Dejar de Seguir
                                    </button>
                                )
                                : (
                                    <button className='btn btn-sm bg-[#3A3A3A] text-white absolute top-0 -right-2 rounded-md'
                                    onClick={() => handleFollow(profile?.user?._id)}
                                    >
                                    { profile?.areYouFollowingMe ? "Seguir también" : "Seguir"}
                                    </button>
                                )
                            }
                        </div>

                        <div className='flex justify-between py-4'>
                            <Link to={`siguiendo`} className='flex flex-col items-center justify-center'>
                                <span>{counters?.following}</span>
                                <p className='text-sm'>Siguiendo</p>
                            </Link>
                            <Link to={`seguidores`} className='flex flex-col items-center justify-center'>
                                <span>{counters?.followers}</span>
                                <p className='text-sm'>Seguidores</p>
                            </Link>
                        </div>

                        <div className='flex flex-col gap-2 py-3'>
                            {profile?.user?.country == "" ? null : (
                                <p className='flex items-center gap-2 text-gray-450'><LuMapPin /> {profile?.user?.country}</p>
                            )}
                            {profile?.user?.link == "" ? null : (
                                <p className='flex items-center gap-2 text-gray-450'><FaLink /> {profile?.user?.link}</p>
                            )}
                        </div>
                    </div>
                )}

                {loadingPots && !loading && (
                    <div className='flex justify-center w-full'>
                        <span className="loading loading-spinner loading-xs mt-4"></span>
                    </div>
                )}
                {loadingPots == false && (
                    <div className='mt-9 px-2'>
                        {
                            posts.length == 0 ? (
                                <p>No hay publicaciones </p>
                            ) : (
                                <PostList posts={posts} setPosts={setPosts} />
                            )
                        }
                       
                    </div>
                )}
        </div>
    )

}
