function D = cachedSqrDist(x,t,w);
% cachedSqrDist : calculate a nt*nx matrix containing weighted squared error between
%                 every vector in x and every vector in t
%                 CACHED so that if only w changes, no complete recalculation needed
% D = sqrDist(x,t,w)
%	x - d*nx matrix containing the x vectors 
%	t - d*nt matrix containing the t vectors
%	w - if specified, a length d weight vector
%	D - the nt*nx result

% Copyright (c) 1995-2001 Frank Dellaert
% All rights Reserved

[d ,nx] = size(x);
[dt,nt] = size(t);
if (dt~=d), error('sqrDist: x and t must have same dimension'); end
if (nargin<3), w=[]; end

global cached_D cached_x cached_t cached_nt cached_nx cached_d;
D = zeros(nt,nx);

cached = 0;
if ~isempty(cached_nt)
   if (cached_nt==nt & cached_nx==nx & cached_d==d)
      cached = isequal(cached_t,t) & isequal(cached_x,x);
   end
end

if (cached)
   % we have cached this information, let's use it
   LD=zeros(1,nt*nx);
   for l=1:d
      LD = LD + (w(l)^2)*cached_D(l,:);    % sum to get weighted distance
   end
   D = reshape(LD,nt,nx);                  % reshape result
else
   % no cache, do the normal thing
   cached_D = zeros(d,nt*nx);              % prepare cache
   for l=1:d
      D2 = dist1(x(l,:),t(l,:)).^2;        % distance for 1 dimension, unweighted
      cached_D(l,:) = reshape(D2,1,nt*nx); % fill cache
      if isempty(w)
         D  = D + D2;                      % sum to get unweighted distance
      else
         D  = D + (w(l)^2)*D2;             % sum to get weighted distance
      end
   end
   cached_t=t;cached_x=x;cached_nt=nt;cached_nx=nx;cached_d=d;	% keep arrays for comparison
end

return


