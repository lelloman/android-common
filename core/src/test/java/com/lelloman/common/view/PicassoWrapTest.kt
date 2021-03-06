package com.lelloman.common.view

import android.widget.ImageView
import com.lelloman.common.data.MeteredConnectionChecker
import com.nhaarman.mockitokotlin2.*
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.RequestCreator
import io.reactivex.subjects.BehaviorSubject
import org.junit.Test

class PicassoWrapTest {

    private val useMeteredNetwork = BehaviorSubject.create<Boolean>()
    private val meteredConnectionChecker: MeteredConnectionChecker = mock()
    private val picassoRequestCreator: RequestCreator = mock {
        on { networkPolicy(any()) }.thenAnswer { networkPolicy -> networkPolicy.mock }
    }

    private val tested = PicassoWrapImpl(
        useMeteredNetwork = useMeteredNetwork.hide(),
        meteredConnectionChecker = meteredConnectionChecker,
        requestCreatorProvider = { picassoRequestCreator }
    )

    @Test
    fun `does not set offline policy if used metered network is true`() {
        givenUseMeteredNetworkIsTrue()

        tested.loadUrlIntoImageView("", MOCK_IMAGE_VIEW, null)

        verify(picassoRequestCreator, never()).networkPolicy(NetworkPolicy.OFFLINE)
    }

    @Test
    fun `does set offline policy if cannot use metered network and network is metered`() {
        givenUseMeteredNetworkIsFalse()
        givenNetworkIsMetered()

        tested.loadUrlIntoImageView("", MOCK_IMAGE_VIEW, null)

        verify(picassoRequestCreator).networkPolicy(NetworkPolicy.OFFLINE)
    }

    @Test
    fun `does not set offline policy if cannot use metered network and network is not metered`() {
        givenUseMeteredNetworkIsFalse()
        givenNetworkIsNotMetered()

        tested.loadUrlIntoImageView("", MOCK_IMAGE_VIEW, null)

        verify(picassoRequestCreator, never()).networkPolicy(NetworkPolicy.OFFLINE)
    }

    @Test
    fun `does not set placeholder if argument is null`() {
        givenUseMeteredNetworkIsTrue()

        tested.loadUrlIntoImageView("", MOCK_IMAGE_VIEW, null)

        verify(picassoRequestCreator, never()).placeholder(any<Int>())
    }

    @Test
    fun `sets placeholder if argument is passed`() {
        givenUseMeteredNetworkIsTrue()
        val placeHolderId = 1234

        tested.loadUrlIntoImageView("", MOCK_IMAGE_VIEW, placeHolderId)

        verify(picassoRequestCreator).placeholder(placeHolderId)
    }

    private fun givenUseMeteredNetworkIsTrue() {
        useMeteredNetwork.onNext(true)
    }

    private fun givenUseMeteredNetworkIsFalse() {
        useMeteredNetwork.onNext(false)
    }

    private fun givenNetworkIsMetered() {
        whenever(meteredConnectionChecker.isNetworkMetered()).thenReturn(true)
    }

    private fun givenNetworkIsNotMetered() {
        whenever(meteredConnectionChecker.isNetworkMetered()).thenReturn(false)
    }

    private companion object {
        val MOCK_IMAGE_VIEW = MockImageView()
    }

    class MockImageView : ImageView(null, null)
}