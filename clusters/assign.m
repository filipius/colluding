function c = assign(t,z);
% ASSIGN: assign each sample in t to nearest cluster center, i.e. VQ
% c = assign(t,z);
%	t - samples to be assigned
%	z - cluster centers (prototype vectors)
%	c - obtained membership

[d,nt]=size(t);
[d,nc]=size(z);

for j=1:nc,                   % for every cluster
  center = z(:,j);            % get cluster center
  D(j,:) = sqrDist(t,center); % calculate the sqr distances from x to center
end

% find minimum
[Dmin,index] = min(D);
c = index;

