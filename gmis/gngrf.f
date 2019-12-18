       integer maxm
       parameter (maxm=100000)
       integer n,m,i,j,v1(maxm),v2(maxm),seed
       real randp,p

       read *,n,p,seed

       m=0
       do 10 i=1,n-1
            do 20 j=i+1,n
                 if (randp(seed).lt.p) then
                      m=m+1
                      v1(m)=i
                      v2(m)=j
                 endif
20          continue
10     continue
       print *,n,m
       do 30 i=1,m
            print *,v1(i),v2(i)
30     continue
       stop
       end
       real function randp(ix)
c      -----------------------------------------------------------------
c
c      randp: Portable pseudo-random number generator.
c             Reference: L. Schrage, "A More Portable Fortran
c             Random Number Generator", ACM Transactions on
c             Mathematical Software, Vol. 2, No. 2, (June, 1979).
c
c      -----------------------------------------------------------------

       integer a,p,ix,b15,b16,xhi,xalo,leftlo,fhi,k
       data a/16807/,b15/32768/,b16/65536/,p/2147483647/

       xhi=ix/b16
       xalo=(ix-xhi*b16)*a
       leftlo=xalo/b16
       fhi=xhi*a+leftlo
       k=fhi/b15
       ix=(((xalo-leftlo*b16)-p)+(fhi-k*b15)*b16)+k
       if (ix.lt.0) ix=ix+p

       randp=float(ix)*4.656612875e-10

       return
       end

