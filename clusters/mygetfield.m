function value=mygetfield(s,f,default)
% mygetfield: getfield with a default value
% value=mygetfield(s,f,default)

global mygetfield_warning
if isempty(mygetfield_warning)
   warning('mygetfield: please compile mex-version by "mex mygetfield.c" in "util" directory');
   mygetfield_warning=1;
end

if isfield(s,f)
   value=getfield(s,f);
else
   value=default;
end